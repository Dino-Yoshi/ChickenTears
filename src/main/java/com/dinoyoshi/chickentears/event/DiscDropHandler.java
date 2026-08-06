package com.dinoyoshi.chickentears.event;

import com.dinoyoshi.chickentears.drop.DropEligibility;
import com.dinoyoshi.chickentears.drop.LavaChickenDropContext;
import com.dinoyoshi.chickentears.drop.TearsDropContext;
import com.dinoyoshi.chickentears.init.ModItems;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DiscDropHandler {
    public static final DiscDropHandler INSTANCE = new DiscDropHandler();

    private static final int SNAPSHOT_TTL_TICKS = 200;
    private static final Map<UUID, LavaChickenDeathSnapshot> LAVA_CHICKEN_SNAPSHOTS =
        new ConcurrentHashMap<UUID, LavaChickenDeathSnapshot>();

    private DiscDropHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onLivingDeath(LivingDeathEvent event) {
        World world = event.getEntityLiving().world;
        purgeStaleSnapshots(world);

        UUID entityUuid = event.getEntityLiving().getUniqueID();
        if (event.isCanceled() || world.isRemote) {
            LAVA_CHICKEN_SNAPSHOTS.remove(entityUuid);
            return;
        }

        ResourceLocation registryId = EntityList.getKey(event.getEntityLiving());
        if (registryId == null || !DropEligibility.isTargetLavaChickenEntity(registryId.toString())) {
            LAVA_CHICKEN_SNAPSHOTS.remove(entityUuid);
            return;
        }

        boolean child = event.getEntityLiving().isChild();
        boolean ridingChicken = event.getEntityLiving().getRidingEntity() instanceof EntityChicken;
        boolean playerAttributedDamage = event.getSource().getTrueSource() instanceof EntityPlayer;
        LavaChickenDropContext context = new LavaChickenDropContext(
            registryId.toString(),
            child,
            ridingChicken,
            playerAttributedDamage,
            isMobLootEnabled(world)
        );

        if (DropEligibility.canDropLavaChicken(context)) {
            LAVA_CHICKEN_SNAPSHOTS.put(entityUuid, new LavaChickenDeathSnapshot(
                registryId.toString(),
                child,
                ridingChicken,
                playerAttributedDamage,
                world.provider.getDimension(),
                world.getTotalWorldTime()
            ));
        } else {
            LAVA_CHICKEN_SNAPSHOTS.remove(entityUuid);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onLivingDrops(LivingDropsEvent event) {
        World world = event.getEntityLiving().world;
        purgeStaleSnapshots(world);

        UUID entityUuid = event.getEntityLiving().getUniqueID();
        LavaChickenDeathSnapshot lavaChickenSnapshot = LAVA_CHICKEN_SNAPSHOTS.remove(entityUuid);

        if (event.isCanceled() || world.isRemote || !isMobLootEnabled(world)) {
            return;
        }

        if (DropEligibility.canDropTears(createTearsContext(event))) {
            event.getDrops().add(createDrop(world, event.getEntityLiving(), new ItemStack(ModItems.MUSIC_DISC_TEARS)));
        }

        if (lavaChickenSnapshot != null
            && lavaChickenSnapshot.dimension == world.provider.getDimension()
            && DropEligibility.canDropLavaChicken(lavaChickenSnapshot.toContext(true))) {
            event.getDrops().add(createDrop(world, event.getEntityLiving(), new ItemStack(ModItems.MUSIC_DISC_LAVA_CHICKEN)));
        }
    }

    private TearsDropContext createTearsContext(LivingDropsEvent event) {
        boolean victimIsGhast = event.getEntityLiving() instanceof EntityGhast;
        UUID victimUuid = event.getEntityLiving().getUniqueID();
        DamageSource source = event.getSource();
        Entity immediateSource = source.getImmediateSource();

        if (!(immediateSource instanceof EntityLargeFireball)) {
            return new TearsDropContext(victimIsGhast, victimUuid, false, null, false, isMobLootEnabled(event.getEntityLiving().world));
        }

        EntityLargeFireball fireball = (EntityLargeFireball) immediateSource;
        return new TearsDropContext(
            victimIsGhast,
            victimUuid,
            true,
            readStoredGhastUuid(fireball),
            fireball.shootingEntity instanceof EntityPlayer && source.getTrueSource() instanceof EntityPlayer,
            isMobLootEnabled(event.getEntityLiving().world)
        );
    }

    private static UUID readStoredGhastUuid(EntityLargeFireball fireball) {
        NBTTagCompound entityData = fireball.getEntityData();
        if (!entityData.hasKey(FireballOriginTracker.ORIGINAL_GHAST_UUID_MOST)
            || !entityData.hasKey(FireballOriginTracker.ORIGINAL_GHAST_UUID_LEAST)) {
            return null;
        }

        return new UUID(
            entityData.getLong(FireballOriginTracker.ORIGINAL_GHAST_UUID_MOST),
            entityData.getLong(FireballOriginTracker.ORIGINAL_GHAST_UUID_LEAST)
        );
    }

    private static EntityItem createDrop(World world, Entity entity, ItemStack stack) {
        return new EntityItem(world, entity.posX, entity.posY, entity.posZ, stack);
    }

    private static boolean isMobLootEnabled(World world) {
        return world.getGameRules().getBoolean("doMobLoot");
    }

    private static void purgeStaleSnapshots(World world) {
        if (world == null || world.isRemote) {
            return;
        }

        int dimension = world.provider.getDimension();
        long now = world.getTotalWorldTime();
        Iterator<Map.Entry<UUID, LavaChickenDeathSnapshot>> iterator = LAVA_CHICKEN_SNAPSHOTS.entrySet().iterator();
        while (iterator.hasNext()) {
            LavaChickenDeathSnapshot snapshot = iterator.next().getValue();
            if (snapshot.dimension == dimension && now - snapshot.createdAtWorldTime > SNAPSHOT_TTL_TICKS) {
                iterator.remove();
            }
        }
    }

    private static final class LavaChickenDeathSnapshot {
        private final String registryId;
        private final boolean child;
        private final boolean ridingChicken;
        private final boolean playerAttributedDamage;
        private final int dimension;
        private final long createdAtWorldTime;

        private LavaChickenDeathSnapshot(
            String registryId,
            boolean child,
            boolean ridingChicken,
            boolean playerAttributedDamage,
            int dimension,
            long createdAtWorldTime
        ) {
            this.registryId = registryId;
            this.child = child;
            this.ridingChicken = ridingChicken;
            this.playerAttributedDamage = playerAttributedDamage;
            this.dimension = dimension;
            this.createdAtWorldTime = createdAtWorldTime;
        }

        private LavaChickenDropContext toContext(boolean mobLootEnabled) {
            return new LavaChickenDropContext(
                registryId,
                child,
                ridingChicken,
                playerAttributedDamage,
                mobLootEnabled
            );
        }
    }
}

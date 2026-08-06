package com.dinoyoshi.chickentears.event;

import java.util.UUID;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class FireballOriginTracker {
    public static final FireballOriginTracker INSTANCE = new FireballOriginTracker();

    public static final String ORIGINAL_GHAST_UUID_MOST = "chickentears.original_ghast_uuid_most";
    public static final String ORIGINAL_GHAST_UUID_LEAST = "chickentears.original_ghast_uuid_least";

    private FireballOriginTracker() {
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote || !(event.getEntity() instanceof EntityLargeFireball)) {
            return;
        }

        EntityLargeFireball fireball = (EntityLargeFireball) event.getEntity();
        if (!(fireball.shootingEntity instanceof EntityGhast)) {
            return;
        }

        NBTTagCompound entityData = fireball.getEntityData();
        if (entityData.hasKey(ORIGINAL_GHAST_UUID_MOST) || entityData.hasKey(ORIGINAL_GHAST_UUID_LEAST)) {
            return;
        }

        UUID ghastUuid = fireball.shootingEntity.getUniqueID();
        entityData.setLong(ORIGINAL_GHAST_UUID_MOST, ghastUuid.getMostSignificantBits());
        entityData.setLong(ORIGINAL_GHAST_UUID_LEAST, ghastUuid.getLeastSignificantBits());
    }
}

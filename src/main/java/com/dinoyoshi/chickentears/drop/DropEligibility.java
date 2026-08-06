package com.dinoyoshi.chickentears.drop;

public final class DropEligibility {
    private static final String ZOMBIE = "minecraft:zombie";
    private static final String ZOMBIE_VILLAGER = "minecraft:zombie_villager";

    private DropEligibility() {
    }

    public static boolean canDropTears(TearsDropContext context) {
        return context.isMobLootEnabled()
            && context.isVictimGhast()
            && context.isImmediateSourceLargeFireball()
            && context.getVictimUuid() != null
            && context.getVictimUuid().equals(context.getOriginalGhastUuid())
            && context.isPlayerDeflectedFireball();
    }

    public static boolean canDropLavaChicken(LavaChickenDropContext context) {
        return context.isMobLootEnabled()
            && isTargetLavaChickenEntity(context.getEntityRegistryId())
            && context.isChild()
            && context.isRidingChickenAtFatalHit()
            && context.isPlayerAttributedFatalDamage();
    }

    public static boolean isTargetLavaChickenEntity(String entityRegistryId) {
        return ZOMBIE.equals(entityRegistryId) || ZOMBIE_VILLAGER.equals(entityRegistryId);
    }
}

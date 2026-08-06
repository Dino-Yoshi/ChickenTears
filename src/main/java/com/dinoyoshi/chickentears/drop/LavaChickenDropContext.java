package com.dinoyoshi.chickentears.drop;

public final class LavaChickenDropContext {
    private final String entityRegistryId;
    private final boolean child;
    private final boolean ridingChickenAtFatalHit;
    private final boolean playerAttributedFatalDamage;
    private final boolean mobLootEnabled;

    public LavaChickenDropContext(
        String entityRegistryId,
        boolean child,
        boolean ridingChickenAtFatalHit,
        boolean playerAttributedFatalDamage,
        boolean mobLootEnabled
    ) {
        this.entityRegistryId = entityRegistryId;
        this.child = child;
        this.ridingChickenAtFatalHit = ridingChickenAtFatalHit;
        this.playerAttributedFatalDamage = playerAttributedFatalDamage;
        this.mobLootEnabled = mobLootEnabled;
    }

    public String getEntityRegistryId() {
        return entityRegistryId;
    }

    public boolean isChild() {
        return child;
    }

    public boolean isRidingChickenAtFatalHit() {
        return ridingChickenAtFatalHit;
    }

    public boolean isPlayerAttributedFatalDamage() {
        return playerAttributedFatalDamage;
    }

    public boolean isMobLootEnabled() {
        return mobLootEnabled;
    }
}

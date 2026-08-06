package com.dinoyoshi.chickentears.drop;

import java.util.UUID;

public final class TearsDropContext {
    private final boolean victimGhast;
    private final UUID victimUuid;
    private final boolean immediateSourceLargeFireball;
    private final UUID originalGhastUuid;
    private final boolean playerDeflectedFireball;
    private final boolean mobLootEnabled;

    public TearsDropContext(
        boolean victimGhast,
        UUID victimUuid,
        boolean immediateSourceLargeFireball,
        UUID originalGhastUuid,
        boolean playerDeflectedFireball,
        boolean mobLootEnabled
    ) {
        this.victimGhast = victimGhast;
        this.victimUuid = victimUuid;
        this.immediateSourceLargeFireball = immediateSourceLargeFireball;
        this.originalGhastUuid = originalGhastUuid;
        this.playerDeflectedFireball = playerDeflectedFireball;
        this.mobLootEnabled = mobLootEnabled;
    }

    public boolean isVictimGhast() {
        return victimGhast;
    }

    public UUID getVictimUuid() {
        return victimUuid;
    }

    public boolean isImmediateSourceLargeFireball() {
        return immediateSourceLargeFireball;
    }

    public UUID getOriginalGhastUuid() {
        return originalGhastUuid;
    }

    public boolean isPlayerDeflectedFireball() {
        return playerDeflectedFireball;
    }

    public boolean isMobLootEnabled() {
        return mobLootEnabled;
    }
}

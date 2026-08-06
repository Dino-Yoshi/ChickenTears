package com.dinoyoshi.chickentears.drop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.Test;

public class DropEligibilityTest {
    private static final UUID GHAST_UUID = UUID.fromString("6f2f6edc-9cbc-41c9-a802-d1c140ea49d2");
    private static final UUID OTHER_GHAST_UUID = UUID.fromString("1b8f7575-53d2-4275-a886-1641b29c4911");

    @Test
    public void tearsDropsForMatchingGhastFireballOriginAndPlayerDeflection() {
        assertTrue(DropEligibility.canDropTears(tears(true, GHAST_UUID, true, GHAST_UUID, true, true)));
    }

    @Test
    public void tearsRejectsNonMatchingCases() {
        assertFalse(DropEligibility.canDropTears(tears(false, GHAST_UUID, true, GHAST_UUID, true, true)));
        assertFalse(DropEligibility.canDropTears(tears(true, GHAST_UUID, false, GHAST_UUID, true, true)));
        assertFalse(DropEligibility.canDropTears(tears(true, GHAST_UUID, true, null, true, true)));
        assertFalse(DropEligibility.canDropTears(tears(true, GHAST_UUID, true, OTHER_GHAST_UUID, true, true)));
        assertFalse(DropEligibility.canDropTears(tears(true, GHAST_UUID, true, GHAST_UUID, false, true)));
        assertFalse(DropEligibility.canDropTears(tears(true, GHAST_UUID, true, GHAST_UUID, true, false)));
    }

    @Test
    public void lavaChickenDropsForBabyZombieOrZombieVillagerChickenJockeyKilledByPlayer() {
        assertTrue(DropEligibility.canDropLavaChicken(lava("minecraft:zombie", true, true, true, true)));
        assertTrue(DropEligibility.canDropLavaChicken(lava("minecraft:zombie_villager", true, true, true, true)));
    }

    @Test
    public void lavaChickenRejectsNonMatchingCases() {
        assertFalse(DropEligibility.canDropLavaChicken(lava("minecraft:husk", true, true, true, true)));
        assertFalse(DropEligibility.canDropLavaChicken(lava("minecraft:zombie_pigman", true, true, true, true)));
        assertFalse(DropEligibility.canDropLavaChicken(lava("example:zombie", true, true, true, true)));
        assertFalse(DropEligibility.canDropLavaChicken(lava("minecraft:zombie", false, true, true, true)));
        assertFalse(DropEligibility.canDropLavaChicken(lava("minecraft:zombie", true, false, true, true)));
        assertFalse(DropEligibility.canDropLavaChicken(lava("minecraft:zombie", true, true, false, true)));
        assertFalse(DropEligibility.canDropLavaChicken(lava("minecraft:zombie", true, true, true, false)));
    }

    @Test
    public void lavaChickenTargetIdsAreExact() {
        assertTrue(DropEligibility.isTargetLavaChickenEntity("minecraft:zombie"));
        assertTrue(DropEligibility.isTargetLavaChickenEntity("minecraft:zombie_villager"));
        assertFalse(DropEligibility.isTargetLavaChickenEntity("minecraft:husk"));
        assertFalse(DropEligibility.isTargetLavaChickenEntity("minecraft:zombie_villager_extra"));
        assertFalse(DropEligibility.isTargetLavaChickenEntity(null));
    }

    private static TearsDropContext tears(
        boolean victimGhast,
        UUID victimUuid,
        boolean immediateSourceLargeFireball,
        UUID originalGhastUuid,
        boolean playerDeflectedFireball,
        boolean mobLootEnabled
    ) {
        return new TearsDropContext(
            victimGhast,
            victimUuid,
            immediateSourceLargeFireball,
            originalGhastUuid,
            playerDeflectedFireball,
            mobLootEnabled
        );
    }

    private static LavaChickenDropContext lava(
        String registryId,
        boolean child,
        boolean ridingChickenAtFatalHit,
        boolean playerAttributedFatalDamage,
        boolean mobLootEnabled
    ) {
        return new LavaChickenDropContext(
            registryId,
            child,
            ridingChickenAtFatalHit,
            playerAttributedFatalDamage,
            mobLootEnabled
        );
    }
}

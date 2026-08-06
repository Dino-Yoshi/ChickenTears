package com.dinoyoshi.chickentears.init;

import com.dinoyoshi.chickentears.ChickenTears;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ChickenTears.MODID)
public final class ModSounds {
    public static final SoundEvent RECORD_TEARS = create("record.tears");
    public static final SoundEvent RECORD_LAVA_CHICKEN = create("record.lava_chicken");

    private ModSounds() {
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(RECORD_TEARS);
        event.getRegistry().register(RECORD_LAVA_CHICKEN);
    }

    private static SoundEvent create(String name) {
        ResourceLocation location = new ResourceLocation(ChickenTears.MODID, name);
        return new SoundEvent(location).setRegistryName(location);
    }
}

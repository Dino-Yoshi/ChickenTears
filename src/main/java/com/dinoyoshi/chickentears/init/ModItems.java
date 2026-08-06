package com.dinoyoshi.chickentears.init;

import com.dinoyoshi.chickentears.ChickenTears;
import com.dinoyoshi.chickentears.item.ItemBackportedRecord;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ChickenTears.MODID)
public final class ModItems {
    public static final ItemBackportedRecord MUSIC_DISC_TEARS =
        new ItemBackportedRecord("music_disc_tears", "tears", ModSounds.RECORD_TEARS);
    public static final ItemBackportedRecord MUSIC_DISC_LAVA_CHICKEN =
        new ItemBackportedRecord("music_disc_lava_chicken", "lava_chicken", ModSounds.RECORD_LAVA_CHICKEN);

    private ModItems() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(MUSIC_DISC_TEARS);
        event.getRegistry().register(MUSIC_DISC_LAVA_CHICKEN);
    }
}

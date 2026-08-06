package com.dinoyoshi.chickentears.client;

import com.dinoyoshi.chickentears.ChickenTears;
import com.dinoyoshi.chickentears.init.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ChickenTears.MODID, value = Side.CLIENT)
public final class ClientModelRegistration {
    private ClientModelRegistration() {
    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        register(ModItems.MUSIC_DISC_TEARS);
        register(ModItems.MUSIC_DISC_LAVA_CHICKEN);
    }

    private static void register(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}

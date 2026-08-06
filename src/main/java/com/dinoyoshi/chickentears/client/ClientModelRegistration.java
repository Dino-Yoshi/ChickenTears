package com.dinoyoshi.chickentears.client;

import com.dinoyoshi.chickentears.init.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientModelRegistration {
    private ClientModelRegistration() {
    }

    public static void registerItemModels() {
        register(ModItems.MUSIC_DISC_TEARS);
        register(ModItems.MUSIC_DISC_LAVA_CHICKEN);
    }

    private static void register(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}

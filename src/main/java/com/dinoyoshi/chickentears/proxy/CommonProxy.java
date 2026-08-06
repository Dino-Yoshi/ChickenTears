package com.dinoyoshi.chickentears.proxy;

import com.dinoyoshi.chickentears.event.DiscDropHandler;
import com.dinoyoshi.chickentears.event.FireballOriginTracker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    private static boolean gameplayHandlersRegistered;

    public void preInit(FMLPreInitializationEvent event) {
        registerGameplayHandlers();
    }

    public void init(FMLInitializationEvent event) {
    }

    private static synchronized void registerGameplayHandlers() {
        if (gameplayHandlersRegistered) {
            return;
        }

        MinecraftForge.EVENT_BUS.register(FireballOriginTracker.INSTANCE);
        MinecraftForge.EVENT_BUS.register(DiscDropHandler.INSTANCE);
        gameplayHandlersRegistered = true;
    }
}

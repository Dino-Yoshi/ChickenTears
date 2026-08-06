package com.dinoyoshi.chickentears.proxy;

import com.dinoyoshi.chickentears.client.ClientModelRegistration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientModelRegistration.registerItemModels();
    }
}

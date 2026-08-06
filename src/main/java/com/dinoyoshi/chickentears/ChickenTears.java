package com.dinoyoshi.chickentears;

import com.dinoyoshi.chickentears.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = ChickenTears.MODID,
    name = ChickenTears.NAME,
    version = ChickenTears.VERSION,
    acceptedMinecraftVersions = "[1.12.2]"
)
public class ChickenTears {
    public static final String MODID = "chickentears";
    public static final String NAME = "Chicken Tears";
    public static final String VERSION = "1.0.0";

    private static final String CLIENT_PROXY = "com.dinoyoshi.chickentears.proxy.ClientProxy";
    private static final String COMMON_PROXY = "com.dinoyoshi.chickentears.proxy.CommonProxy";

    @Mod.Instance(MODID)
    public static ChickenTears instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        logger.info("{} initialized for Minecraft 1.12.2", NAME);
    }
}

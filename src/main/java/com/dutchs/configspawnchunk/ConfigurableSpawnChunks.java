package com.dutchs.configspawnchunk;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MODID)
public class ConfigurableSpawnChunks {
    private static final Logger LOGGER = LogManager.getLogger(Constants.MODID);

    public static void logInfo(String msg) {
        LOGGER.info("[" + Constants.MODNAME + "] " + msg);
    }

    public static int spawnChunkCount = 0;

    public ConfigurableSpawnChunks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        logInfo("Let us rain some doom down upon the heads of our doomed enemies.");
    }
}

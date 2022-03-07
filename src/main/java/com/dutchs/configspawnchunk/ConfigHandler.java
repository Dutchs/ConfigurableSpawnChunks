package com.dutchs.configspawnchunk;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
    static final ServerConfig SERVER_CONFIG;
    static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_CONFIG = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }

    public static int chunkDistance;

    public static void bakeConfig() {
        chunkDistance = SERVER_CONFIG.chunkDistance.get();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == ConfigHandler.SERVER_SPEC) {
            bakeConfig();
        }
    }

    public static class ServerConfig {
        public ForgeConfigSpec.IntValue chunkDistance;
        public ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("MinecraftServer");
            chunkDistance = builder.comment("Chunk Distance as used by ServerChunkCache->addRegionTicket() (0 = skip generating chunks, 11 = vanilla)",
                                            "Actual logic used: ((chunkDistance * 2) - 1) ^ 2 = totalChunkCount, taking vanilla as a example: ((11 * 2) - 1) ^ 2 = 441 chunks")
                    .defineInRange("chunkDistance", Constants.DEFAULT_CHUNK_DISTANCE, 0, 32);
            builder.pop();
        }
    }
}


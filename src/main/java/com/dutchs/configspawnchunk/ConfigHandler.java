package com.dutchs.configspawnchunk;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
    static final ServerConfig SERVER_CONFIG;
    static final ForgeConfigSpec SERVER_SPEC;

    static final ClientConfig CLIENT_CONFIG;
    static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPairServer = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_CONFIG = specPairServer.getLeft();
        SERVER_SPEC = specPairServer.getRight();

        final Pair<ClientConfig, ForgeConfigSpec> specPairCommon = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_CONFIG = specPairCommon.getLeft();
        CLIENT_SPEC = specPairCommon.getRight();
    }

    //Server
    public static int chunkDistance;

    public static void bakeServerConfig() {
        chunkDistance = SERVER_CONFIG.chunkDistance.get();
    }

    //Client
    public static boolean alwaysOverrideProgressIndicator;
    public static int animationFrameTime;
    public static List<? extends String> progressAnimation;

    public static boolean readDataTextOverride;
    public static List<? extends String> readDataText;

    public static boolean preparingWorldTextOverride;
    public static List<? extends String> preparingWorldText;

    public static boolean joiningWorldTextOverride;
    public static List<? extends String> joiningWorldText;

    public static void bakeClientConfig() {
        alwaysOverrideProgressIndicator = CLIENT_CONFIG.alwaysOverrideProgressIndicator.get();
        animationFrameTime = CLIENT_CONFIG.animationFrameTime.get();
        progressAnimation = new ArrayList<>(CLIENT_CONFIG.progressAnimation.get());

        readDataTextOverride = CLIENT_CONFIG.readDataTextOverride.get();
        readDataText = new ArrayList<>(CLIENT_CONFIG.readDataText.get());

        preparingWorldTextOverride = CLIENT_CONFIG.preparingWorldTextOverride.get();
        preparingWorldText = new ArrayList<>(CLIENT_CONFIG.preparingWorldText.get());

        joiningWorldTextOverride = CLIENT_CONFIG.joiningWorldTextOverride.get();
        joiningWorldText = new ArrayList<>(CLIENT_CONFIG.joiningWorldText.get());
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == ConfigHandler.SERVER_SPEC) {
            bakeServerConfig();
        } else if (configEvent.getConfig().getSpec() == ConfigHandler.CLIENT_SPEC) {
            bakeClientConfig();
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

    public static class ClientConfig {
        public ForgeConfigSpec.BooleanValue alwaysOverrideProgressIndicator;
        public ForgeConfigSpec.IntValue animationFrameTime;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> progressAnimation;
        List<String> defaultAnimation = Arrays.asList("[#####]", "[\u00a7c#\u00a7r####]", "[\u00a74#\u00a7c#\u00a7r###]", "[\u00a7c#\u00a74#\u00a7c#\u00a7r##]", "[#\u00a7c#\u00a74#\u00a7c#\u00a7r#]", "[##\u00a7c#\u00a74#\u00a7c#\u00a7r]", "[###\u00a7c#\u00a74#\u00a7r]", "[####\u00a7c#\u00a7r]");

        public ForgeConfigSpec.BooleanValue preparingWorldTextOverride;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> preparingWorldText;
        List<String> defaultPreparingWorld = Arrays.asList("Preparing for your doom...", "Prepare to meet your horrible doom!");

        public ForgeConfigSpec.BooleanValue readDataTextOverride;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> readDataText;
        List<String> defaultReadData = Arrays.asList("Abandoning all hope...", "The knowledge... It fills me! It is neat!");

        public ForgeConfigSpec.BooleanValue joiningWorldTextOverride;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> joiningWorldText;
        List<String> defaultJoiningWorld = Arrays.asList("I'm an unstoppable death machine, you know", "Dance with us, Dance with us into oblivion!", "Yayyyy! It BURNS!");

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            Predicate<Object> validator = o -> o instanceof String;
            builder.push("Progress Indicator");
            alwaysOverrideProgressIndicator = builder.comment("Always use custom progress indicator, chunkDistance == 0 is always overridden")
                    .define("alwaysOverrideProgressIndicator", false);
            animationFrameTime = builder.comment("Delay between frames in milliseconds, ignored when using single progressAnimation frame")
                    .defineInRange("animationFrameTime", 333, 1, Integer.MAX_VALUE);
            progressAnimation = builder.comment("Animation frames to use for progress indicator, can use vanilla Formatting Codes",
                            "https://minecraft.fandom.com/wiki/Formatting_codes#Use_in_server.properties_and_pack.mcmeta")
                    .defineList("progressAnimation", defaultAnimation, validator);
            builder.pop();

            builder.push("World Create/Load Text");
            preparingWorldTextOverride = builder.comment("Use (randomized) text for createWorld.preparing")
                    .define("preparingWorldTextOverride", false);
            preparingWorldText = builder.comment("Random text used for createWorld.preparing, can use vanilla Formatting Codes",
                            "https://minecraft.fandom.com/wiki/Formatting_codes#Use_in_server.properties_and_pack.mcmeta")
                    .defineList("preparingWorldText", defaultPreparingWorld, validator);
            readDataTextOverride = builder.comment("Use (randomized) text for selectWorld.data_read")
                    .define("readDataTextOverride", false);
            readDataText = builder.comment("Random text used for selectWorld.data_read, can use vanilla Formatting Codes",
                            "https://minecraft.fandom.com/wiki/Formatting_codes#Use_in_server.properties_and_pack.mcmeta")
                    .defineList("readDataText", defaultReadData, validator);
            joiningWorldTextOverride = builder.comment("Use (randomized) text for connect.joining")
                    .define("joiningWorldTextOverride", false);
            joiningWorldText = builder.comment("Random text used for connect.joining, can use vanilla Formatting Codes",
                            "https://minecraft.fandom.com/wiki/Formatting_codes#Use_in_server.properties_and_pack.mcmeta")
                    .defineList("joiningWorldText", defaultJoiningWorld, validator);
            builder.pop();
        }
    }
}


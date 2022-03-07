package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigHandler;
import com.dutchs.configspawnchunk.ConfigurableSpawnChunks;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    //private static int DISTANCE = 3; //11
    private static int EXPECTED_CHUNK_COUNT = 0;

    //serverchunkcache.addRegionTicket(TicketType.START, new ChunkPos(blockpos), 11, Unit.INSTANCE) -> [11]
    //@ModifyArg(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;addRegionTicket(Lnet/minecraft/server/level/TicketType;Lnet/minecraft/world/level/ChunkPos;ILjava/lang/Object;)V"), index = 2)
    @ModifyConstant(method = "prepareLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V", constant = @Constant(intValue = 11))
    private int onPrepareLevels_AddRegionTicket_Constant(int value) {
        ConfigurableSpawnChunks.logInfo("ChunkDistance: " + ConfigHandler.chunkDistance);
        return ConfigHandler.chunkDistance; //11
    }

    //this.progressListenerFactory.create(11) -> [11]
    //@ModifyArg(method = "loadLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/progress/ChunkProgressListenerFactory;create(I)Lnet/minecraft/server/level/progress/ChunkProgressListener;"))
    @ModifyConstant(method = "loadLevel", constant = @Constant(intValue = 11))
    private int onLoadLevel_ChunkProgressListenerFactory_Create_Constant(int value) {
        int actualRadius = Math.max((ConfigHandler.chunkDistance * 2) - 1, 0);
        EXPECTED_CHUNK_COUNT = actualRadius * actualRadius;
        //ConfigurableSpawnChunks.logInfo("ChunkProgressListenerFactory_Create: " + ConfigHandler.chunkDistance);
        return ConfigHandler.chunkDistance; //11
    }

    //while(serverchunkcache.getTickingGenerated() != 441) -> [441]
    @ModifyConstant(method = "prepareLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V", constant = @Constant(intValue = 441))
    private int onPrepareLevels_TickingGenerated_Constant(int value) {
        //ConfigurableSpawnChunks.logInfo("GEN: " + ServerLifecycleHooks.getCurrentServer().overworld().getChunkSource().getTickingGenerated());
        if(ServerLifecycleHooks.getCurrentServer().overworld().getChunkSource().getTickingGenerated() == EXPECTED_CHUNK_COUNT){
            ConfigurableSpawnChunks.logInfo("Generated: " + EXPECTED_CHUNK_COUNT + " Chunks");
        }
        return EXPECTED_CHUNK_COUNT; //441
    }
}


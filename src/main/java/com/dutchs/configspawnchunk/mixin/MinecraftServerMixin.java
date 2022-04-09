package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigHandler;
import com.dutchs.configspawnchunk.ConfigurableSpawnChunks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    //serverchunkcache.addRegionTicket(TicketType.START, new ChunkPos(blockpos), 11, Unit.INSTANCE) -> [11]
    //@ModifyArg(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;addRegionTicket(Lnet/minecraft/server/level/TicketType;Lnet/minecraft/world/level/ChunkPos;ILjava/lang/Object;)V"), index = 2)
    @ModifyConstant(method = "prepareLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V", constant = @Constant(intValue = 11))
    private int onPrepareLevels_AddRegionTicket_Constant(int value) {
        ConfigurableSpawnChunks.logInfo("ChunkDistance: " + ConfigHandler.chunkDistance);
        return ConfigHandler.chunkDistance; //11
    }

    //this.progressListenerFactory.create(11) -> [11]
    //@ModifyArg(method = "loadLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/progress/ChunkProgressListenerFactory;create(I)Lnet/minecraft/server/level/progress/ChunkProgressListener;"))
    @ModifyConstant(method = "loadLevel()V", constant = @Constant(intValue = 11))
    private int onLoadLevel_ChunkProgressListenerFactory_Create_Constant(int value) {
        int actualRadius = Math.max((ConfigHandler.chunkDistance * 2) - 1, 0);
        ConfigurableSpawnChunks.spawnChunkCount = actualRadius * actualRadius;
        //ConfigurableSpawnChunks.logInfo("ChunkProgressListenerFactory_Create: " + ConfigHandler.chunkDistance);
        return ConfigHandler.chunkDistance; //11
    }

    //while(serverchunkcache.getTickingGenerated() != 441) -> [441]
    @ModifyConstant(method = "prepareLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V", constant = @Constant(intValue = 441))
    private int onPrepareLevels_TickingGenerated_Constant(int value) {
        int generated = ServerLifecycleHooks.getCurrentServer().overworld().getChunkSource().getTickingGenerated();
        //ConfigurableSpawnChunks.logInfo("GEN: " + generated);
        if (generated >= ConfigurableSpawnChunks.spawnChunkCount) {
            ConfigurableSpawnChunks.logInfo("Generated: " + generated + " Chunks");
        }
        return ConfigurableSpawnChunks.spawnChunkCount; //441
    }
}


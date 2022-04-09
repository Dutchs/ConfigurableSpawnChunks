package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigHandler;
import com.dutchs.configspawnchunk.ConfigurableSpawnChunks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends ScreenMixin {
    @Shadow long lastNarration;
    @Shadow StoringChunkProgressListener progressListener;

    //@ModifyArg(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"), index = 2)
    @Overwrite
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        long i = Util.getMillis();
        if (i - this.lastNarration > 2000L) {
            this.lastNarration = i;
            this.triggerImmediateNarration(true);
        }

        int wCentre = this.width / 2;
        int hCentre = this.height / 2;
        int hOffset = 15; //TODO: figure out a good offset based on chunkDistance
        int chunkSize = 2; //2
        int chunkBorder = 0; //0
        LevelLoadingScreen.renderChunks(pPoseStack, this.progressListener, wCentre, hCentre + hOffset, chunkSize, chunkBorder);

        String progressText = "";

        if (ConfigurableSpawnChunks.spawnChunkCount == 0 || ConfigHandler.alwaysOverrideProgressIndicator) {
            List<? extends String> progressIndicator = ConfigHandler.progressAnimation;
            if (progressIndicator.size() > 1) {
                int msPerFrame = ConfigHandler.animationFrameTime;
                float t = Util.getMillis() % ((long) progressIndicator.size() * msPerFrame);
                int idx = Mth.floor(t / msPerFrame);
                progressText = progressIndicator.get(idx);
            } else if (progressIndicator.size() == 1) {
                progressText = progressIndicator.get(0);
            }
        } else {
            progressText = Mth.clamp(this.progressListener.getProgress(), 0, 100) + "%";
        }

        GuiComponent.drawCenteredString(pPoseStack, this.font, progressText, wCentre, hCentre - 9 / 2 - hOffset, 16777215);
    }
}

package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigurableSpawnChunks;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    private static long start = 0;

    @ModifyArg(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"), index = 2)
    private String onRender_DrawCenteredString_Argument(String pText) {
        if (ConfigurableSpawnChunks.spawnChunkCount == 0) {
            int d = (int) (Util.getMillis() % 1000);
            return d < 333 ? "/" : d < 666 ? "-" : "\\";
        }

        return pText;
    }
}

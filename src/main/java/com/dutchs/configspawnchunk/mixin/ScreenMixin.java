package com.dutchs.configspawnchunk.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow abstract void renderBackground(PoseStack pPoseStack);
    @Shadow abstract void triggerImmediateNarration(boolean val);
    @Shadow int width;
    @Shadow int height;
    @Shadow Font font;
    @Shadow Minecraft minecraft;
}

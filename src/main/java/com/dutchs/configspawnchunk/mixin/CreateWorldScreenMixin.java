package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends ScreenMixin {
    private static final Random r = new Random();

    @ModifyArg(method = "onCreate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;forceSetScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private Screen onCreateForceSetScreen(Screen dirtScreen) {
        if (ConfigHandler.preparingWorldTextOverride && ConfigHandler.preparingWorldText.size() > 0) {
            dirtScreen.title = new TextComponent(ConfigHandler.preparingWorldText.get(r.nextInt(ConfigHandler.preparingWorldText.size())));
        }
        return dirtScreen;
    }
}

package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    private static final Random r = new Random();

    //progressscreen.progressStartNoAbort(new TranslatableComponent("connect.joining"));
    @ModifyArg(method = "setLevel(Lnet/minecraft/client/multiplayer/ClientLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ProgressScreen;progressStartNoAbort(Lnet/minecraft/network/chat/Component;)V"))
    private Component onCreateForceSetScreen(Component component) {
        if (ConfigHandler.joiningWorldTextOverride && ConfigHandler.joiningWorldText.size() > 0) {
            return new TextComponent(ConfigHandler.joiningWorldText.get(r.nextInt(ConfigHandler.joiningWorldText.size())));
        } else {
            return component;
        }
    }
}

package com.dutchs.configspawnchunk.mixin;

import com.dutchs.configspawnchunk.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldListEntryMixin {
    private static final Random r = new Random();
    @Shadow Minecraft minecraft;

    //this.minecraft.forceSetScreen(new GenericDirtMessageScreen(new TranslatableComponent("selectWorld.data_read")));
    @Overwrite()
    private void queueLoadScreen() {
        Component textComponent;
        if (ConfigHandler.readDataTextOverride && ConfigHandler.readDataText.size() > 0) {
            textComponent = new TextComponent(ConfigHandler.readDataText.get(r.nextInt(ConfigHandler.readDataText.size())));
        } else {
            textComponent = new TranslatableComponent("selectWorld.data_read");
        }

        minecraft.forceSetScreen(new GenericDirtMessageScreen(textComponent));
    }
}


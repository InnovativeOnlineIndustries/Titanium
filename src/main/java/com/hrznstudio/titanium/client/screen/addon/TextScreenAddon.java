/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class TextScreenAddon extends BasicScreenAddon {

    private String text;
    private boolean shadow;
    private int color;

    public TextScreenAddon(String text, int posX, int posY, boolean shadow, int color) {
        super(posX, posY);
        this.text = text;
        this.shadow = shadow;
        this.color = color;
    }

    public TextScreenAddon(String text, int posX, int posY, boolean shadow) {
        this(text, posX, posY, shadow, 0xFFFFFF);
    }

    @Override
    public int getXSize() {
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        if (shadow) {
            //drawStringWithShadow
            Minecraft.getInstance().font.drawShadow(stack, getText(), guiX + getPosX(), guiY + getPosY(), color);
        } else {
            //drawString
            Minecraft.getInstance().font.draw(stack, getText(), guiX + getPosX(), guiY + getPosY(), color);
        }
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {}

    public String getText() {
        return text;
    }

}

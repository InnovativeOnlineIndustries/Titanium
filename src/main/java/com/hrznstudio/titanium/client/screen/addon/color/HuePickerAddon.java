/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon.color;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;
import java.util.function.Consumer;

public class HuePickerAddon extends BasicScreenAddon {

    private final Consumer<Float> consumer;
    private float hue;

    protected HuePickerAddon(int posX, int posY, float hue, Consumer<Float> consumer) {
        super(posX, posY);
        this.consumer = consumer;
        this.hue = hue;
    }

    @Override
    public int getXSize() {
        return 146;
    }

    @Override
    public int getYSize() {
        return 8;
    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        drawColorBar(stack, this.getPosX() + guiX, this.getPosY() + guiY, this.getXSize(), this.getYSize());
        AssetUtil.drawAsset(stack, screen, provider.getAsset(AssetTypes.HUE_PICKER), guiX + (int) (this.getPosX() + this.hue * this.getXSize()) - 3, guiY + this.getPosY() - 3);
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {

    }

    private void drawColorBar(PoseStack matrix, int x, int y, int width, int height) {
        for (int i = 0; i < width; ++i) {
            GuiComponent.fill(matrix, x + i, y, x + i + 1, y + height, Color.getHSBColor(((float) i / width), 1f, 1f).getRGB());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.hue = (float) ((mouseX - this.getPosX()) / getXSize());
        consumer.accept(hue);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        this.hue = ((float) mouseX - this.getPosX()) / getXSize();
        consumer.accept(hue);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public void setHue(float hue) {
        this.hue = hue;
    }
}

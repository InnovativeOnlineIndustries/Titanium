/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon.color;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.interfaces.ICanMouseDrag;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.util.function.Consumer;

public class HuePickerAddon extends BasicScreenAddon implements IClickable, ICanMouseDrag {

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
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        drawColorBar(stack, this.getPosX() + guiX, this.getPosY() + guiY, this.getXSize(), this.getYSize());
        AssetUtil.drawAsset(stack, screen, provider.getAsset(AssetTypes.HUE_PICKER), guiX + (int) (this.getPosX() + this.hue * this.getXSize()) - 3, guiY + this.getPosY() - 3);
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    private void drawColorBar(MatrixStack matrix, int x, int y, int width, int height) {
        for (int i = 0; i < width; ++i) {
            AbstractGui.fill(matrix, x + i, y, x + i + 1, y + height, Color.getHSBColor(((float) i / width), 1f, 1f).getRGB());
        }
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        this.hue = (float) ((mouseX - this.getPosX() - guiX) / getXSize());
        consumer.accept(hue);
    }

    @Override
    public void drag(int x, int y) {
        this.hue = ((float) x - this.getPosX()) / getXSize();
        consumer.accept(hue);
    }

    public void setHue(float hue) {
        this.hue = hue;
    }
}

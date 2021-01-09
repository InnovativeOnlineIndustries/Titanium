/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon.color;

import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.interfaces.ICanMouseDrag;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.util.function.Consumer;

public class ColorPickerAddon extends BasicScreenAddon implements IClickable, ICanMouseDrag {

    private final Consumer<Integer> colorConsumer;
    private ShadePickerAddon shadePickerAddon;
    private HuePickerAddon huePickerAddon;
    private float hue;
    private float brightness;
    private float saturation;

    public ColorPickerAddon(int posX, int posY, int color, Consumer<Integer> colorConsumer) {
        super(posX, posY);
        setColor(color);
        this.shadePickerAddon = new ShadePickerAddon(posX, posY, this.brightness, this.saturation, () -> hue, bright -> {
            this.brightness = bright;
            updateColor();
        }, sat -> {
            this.saturation = sat;
            updateColor();
        });
        this.huePickerAddon = new HuePickerAddon(posX, posY + 90, this.hue, t -> {
            hue = t;
            updateColor();
        });
        this.colorConsumer = colorConsumer;
        updateColor();
    }

    @Override
    public int getXSize() {
        return this.huePickerAddon.getXSize();
    }

    @Override
    public int getYSize() {
        return this.shadePickerAddon.getYSize() + 10 + this.huePickerAddon.getYSize() + 10;
    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        this.shadePickerAddon.drawBackgroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
        this.huePickerAddon.drawBackgroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
        AbstractGui.fill(stack, guiX + this.getPosX() + 110, guiY + this.getPosY(), guiX + this.getPosX() + this.getXSize(), guiY + this.getPosY() + this.shadePickerAddon.getYSize(), Color.HSBtoRGB(hue, saturation, brightness));
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        this.shadePickerAddon.drawForegroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY);
        this.huePickerAddon.drawForegroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY);
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        if (this.huePickerAddon.isInside(screen, mouseX - guiX, mouseY - guiY)) {
            this.huePickerAddon.handleClick(screen, guiX, guiY, mouseX, mouseY, button);
        }
        if (this.shadePickerAddon.isInside(screen, mouseX - guiX, mouseY - guiY)) {
            this.shadePickerAddon.handleClick(screen, guiX, guiY, mouseX, mouseY, button);
        }
    }

    @Override
    public void drag(int x, int y) {
        if (this.huePickerAddon.isInside(null, x, y)) {
            this.huePickerAddon.drag(x, y);
        }
        if (this.shadePickerAddon.isInside(null, x, y)) {
            this.shadePickerAddon.drag(x, y);
        }
    }

    private void updateColor() {
        colorConsumer.accept(Color.HSBtoRGB(hue, saturation, brightness));
    }

    public void setColor(int color) {
        Color c = new Color(color);
        float[] values = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        this.hue = values[0];
        this.saturation = values[1];
        this.brightness = values[2];
        if (this.huePickerAddon != null) this.huePickerAddon.setHue(this.hue);
        if (this.shadePickerAddon != null) {
            this.shadePickerAddon.setBrightness(this.brightness);
            this.shadePickerAddon.setSaturation(this.saturation);
        }
    }
}

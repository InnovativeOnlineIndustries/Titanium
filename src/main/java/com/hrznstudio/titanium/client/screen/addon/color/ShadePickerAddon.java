/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
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
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShadePickerAddon extends BasicScreenAddon implements IClickable, ICanMouseDrag {

    private static final int S_TILES = 10, V_TILES = 10;
    private final Supplier<Float> hueSupplier;
    private final Consumer<Float> brightnessConsumer;
    private final Consumer<Float> saturationConsumer;
    private float brightness;
    private float saturation;

    public ShadePickerAddon(int posX, int posY, float brightness, float saturation, Supplier<Float> hueSupplier, Consumer<Float> brightnessConsumer, Consumer<Float> saturationConsumer) {
        super(posX, posY);
        this.hueSupplier = hueSupplier;
        this.brightness = brightness;
        this.saturation = saturation;
        this.brightnessConsumer = brightnessConsumer;
        this.saturationConsumer = saturationConsumer;
    }

    @Override
    public int getXSize() {
        return 100;
    }

    @Override
    public int getYSize() {
        return 80;
    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        drawTiledGradient(stack, this.getPosX() + guiX, this.getPosY() + guiY, getXSize(), getYSize());
        AssetUtil.drawAsset(stack, screen, provider.getAsset(AssetTypes.SHADE_PICKER), guiX + (int) (this.getPosX() + this.saturation * this.getXSize()) - 4, (int) (guiY + this.getPosY() + (1 - this.brightness) * this.getYSize()) - 4);
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    private void drawTiledGradient(PoseStack matrix, int x, int y, int width, int height) {
        int tileWidth = Math.round((float) width / S_TILES);
        int tileHeight = Math.round((float) height / V_TILES);
        for (int i = 0; i < 10; i++) {
            float minV = (float) i / V_TILES, maxV = (float) (i + 1) / V_TILES;
            for (int j = 0; j < 10; j++) {
                float minS = (float) j / S_TILES, maxS = (float) (j + 1) / S_TILES;
                Color tl = Color.getHSBColor(hueSupplier.get(), minS, maxV), tr = Color.getHSBColor(hueSupplier.get(), maxS, maxV), bl = Color.getHSBColor(hueSupplier.get(), minS, minV), br = Color.getHSBColor(hueSupplier.get(), maxS, minV);
                drawGradient(matrix, x + j * tileWidth, y + (V_TILES - i - 1) * tileHeight, tileWidth, tileHeight, tl, tr, bl, br);
            }
        }
    }


    private void drawGradient(PoseStack matrix, int x, int y, int width, int height, Color tl, Color tr, Color bl, Color br) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        Matrix4f matrix4f = matrix.last().pose();
        buffer.begin(7, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix4f, x, y + height, 0).color(bl.getRed() / 255f, bl.getGreen() / 255f, bl.getBlue() / 255f, bl.getAlpha() / 255f).endVertex();
        buffer.vertex(matrix4f, x + width, y + height, 0).color(br.getRed() / 255f, br.getGreen() / 255f, br.getBlue() / 255f, br.getAlpha() / 255f).endVertex();
        buffer.vertex(matrix4f, x + width, y, 0).color(tr.getRed() / 255f, tr.getGreen() / 255f, tr.getBlue() / 255f, tr.getAlpha() / 255f).endVertex();
        buffer.vertex(matrix4f, x, y, 0).color(tl.getRed() / 255f, tl.getGreen() / 255f, tl.getBlue() / 255f, tl.getAlpha() / 255f).endVertex();
        buffer.end();
        BufferUploader.end(buffer);
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    @Override
    public void drag(int x, int y) {
        this.saturation = (((float) x - this.getPosX()) / getXSize());
        this.brightness = 1 - (((float) y - this.getPosY()) / getYSize());
        this.saturationConsumer.accept(saturation);
        this.brightnessConsumer.accept(brightness);
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        this.saturation = (float) ((mouseX - this.getPosX() - guiX) / getXSize());
        this.brightness = 1 - (float) ((mouseY - this.getPosY() - guiY) / getYSize());
        this.saturationConsumer.accept(saturation);
        this.brightnessConsumer.accept(brightness);
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }
}

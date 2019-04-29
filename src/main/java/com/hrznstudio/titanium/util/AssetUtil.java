/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;


public class AssetUtil {

    public static void drawAsset(GuiScreen screen, IAsset asset, int xPos, int yPos) {
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        screen.drawTexturedModalRect(xPos + offset.x,
                yPos + offset.y,
                area.x,
                area.y,
                area.width,
                area.height);
    }

    public static void drawSelectingOverlay(int x, int y, int width, int height) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        Gui.drawRect(x, y, width, height, -2130706433);
        GlStateManager.enableLighting();
        GlStateManager.disableDepthTest();
    }

    public static void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        Gui.drawRect(startX, y, endX + 1, y + 1, color);
    }

    public static void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }
        Gui.drawRect(x, startY + 1, x + 1, endY, color);
    }
}

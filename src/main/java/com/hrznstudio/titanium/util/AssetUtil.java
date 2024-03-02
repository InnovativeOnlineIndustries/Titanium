/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;


public class AssetUtil {

    public static void drawAsset(GuiGraphics guiGraphics, Screen screen, IAsset asset, int xPos, int yPos) {
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        guiGraphics.blit(asset.getResourceLocation(), xPos + offset.x,
            yPos + offset.y,
            area.x,
            area.y,
            area.width,
            area.height);
    }

    public static void drawSelectingOverlay(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.fill(x, y, width, height, -2130706433);
    }

    public static void drawHorizontalLine(GuiGraphics guiGraphics, int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        guiGraphics.fill(startX, y, endX + 1, y + 1, color);
    }

    public static void drawVerticalLine(GuiGraphics guiGraphics, int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }
        guiGraphics.fill(x, startY + 1, x + 1, endY, color);
    }
}

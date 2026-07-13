/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.awt.*;


public class AssetUtil {

    public static void drawAsset(GuiGraphicsExtractor guiGraphics, Screen screen, IAsset asset, int xPos, int yPos) {
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        blit(guiGraphics, asset.getIdentifier(), xPos + offset.x,
            yPos + offset.y,
            area.x,
            area.y,
            area.width,
            area.height);
    }

    public static void blit(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, float u, float v, int width, int height) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, 256, 256);
    }

    public static void blit(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, float u, float v, int width, int height, int color) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, 256, 256, color);
    }

    public static int color(float red, float green, float blue, float alpha) {
        return ((int) (alpha * 255) << 24)
            | ((int) (red * 255) << 16)
            | ((int) (green * 255) << 8)
            | (int) (blue * 255);
    }

    public static void drawSelectingOverlay(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height) {
        guiGraphics.fill(x, y, width, height, -2130706433);
    }

    public static void drawHorizontalLine(GuiGraphicsExtractor guiGraphics, int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        guiGraphics.fill(startX, y, endX + 1, y + 1, color);
    }

    public static void drawVerticalLine(GuiGraphicsExtractor guiGraphics, int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }
        guiGraphics.fill(x, startY + 1, x + 1, endY, color);
    }
}

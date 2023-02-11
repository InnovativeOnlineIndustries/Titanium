/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.api.client.IAsset;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;


public class AssetUtil {

    public static void drawAsset(PoseStack stack, Screen screen, IAsset asset, int xPos, int yPos) {
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        RenderSystem.setShaderTexture(0, asset.getResourceLocation());
        screen.blit(stack, xPos + offset.x,
            yPos + offset.y,
            area.x,
            area.y,
            area.width,
            area.height);
    }

    public static void drawSelectingOverlay(PoseStack stack, int x, int y, int width, int height) {
        GuiComponent.fill(stack, x, y, width, height, -2130706433);
    }

    public static void drawHorizontalLine(PoseStack stack, int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        GuiComponent.fill(stack, startX, y, endX + 1, y + 1, color);
    }

    public static void drawVerticalLine(PoseStack stack, int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }
        GuiComponent.fill(stack, x, startY + 1, x + 1, endY, color);
    }
}

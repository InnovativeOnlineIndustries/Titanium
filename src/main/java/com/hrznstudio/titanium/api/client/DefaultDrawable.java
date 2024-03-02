/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public class DefaultDrawable implements IDrawable {
    private final IAsset asset;
    private final PoseStack matrixStack;

    DefaultDrawable(IAsset asset, PoseStack matrixStack) {
        this.asset = asset;
        this.matrixStack = matrixStack;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, Screen gui, Point position, Point mousePosition) {
        Rectangle area = asset.getArea();
        guiGraphics.blit(asset.getResourceLocation(), position.x, position.y, area.x, area.y, area.width, area.height);
    }
}

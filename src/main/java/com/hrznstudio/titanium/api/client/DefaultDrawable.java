/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;

public class DefaultDrawable implements IDrawable {
    private final IAsset asset;
    private final MatrixStack matrixStack;

    DefaultDrawable(IAsset asset, MatrixStack matrixStack) {
        this.asset = asset;
        this.matrixStack = matrixStack;
    }

    @Override
    public void draw(Screen gui, Point position, Point mousePosition) {
        gui.getMinecraft().getTextureManager().bindTexture(asset.getResourceLocation());
        Rectangle area = asset.getArea();
        gui.blit(matrixStack, position.x, position.y, area.x, area.y, area.width, area.height);
    }
}

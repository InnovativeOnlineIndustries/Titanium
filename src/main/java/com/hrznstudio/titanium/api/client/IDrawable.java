/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public interface IDrawable {
    static IDrawable of(IAsset asset, PoseStack matrixStack) {
        return new DefaultDrawable(asset, matrixStack);
    }

    void draw(Screen gui, Point position, Point mousePosition);
}

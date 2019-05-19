/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public interface IDrawable {
    static IDrawable of(IAsset asset) {
        return new DefaultDrawable(asset);
    }

    void draw(GuiScreen gui, Point position, Point mousePosition);
}

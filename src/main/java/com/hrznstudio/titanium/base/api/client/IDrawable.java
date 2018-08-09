/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.api.client;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public interface IDrawable {
    void draw(GuiScreen gui, Point position, Point mousePosition);

    static IDrawable of(IAsset asset) {
        return new DefaultDrawable(asset);
    }
}

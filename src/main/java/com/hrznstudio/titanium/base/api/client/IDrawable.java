/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.api.client;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public interface IDrawable {
    static IDrawable of(IAsset asset) {
        return new DefaultDrawable(asset);
    }

    void draw(GuiScreen gui, Point position, Point mousePosition);
}

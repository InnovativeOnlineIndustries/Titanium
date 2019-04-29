/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
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

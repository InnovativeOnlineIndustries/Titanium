/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import net.minecraft.client.gui.screen.Screen;

import java.awt.*;

public class DefaultDrawable implements IDrawable {
    private final IAsset asset;

    DefaultDrawable(IAsset asset) {
        this.asset = asset;
    }

    @Override
    public void draw(Screen gui, Point position, Point mousePosition) {
        gui.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        Rectangle area = asset.getArea();
        gui.drawTexturedModalRect(position.x, position.y, area.x, area.y, area.width, area.height);
    }
}

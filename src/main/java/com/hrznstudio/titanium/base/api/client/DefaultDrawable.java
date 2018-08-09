/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.api.client;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class DefaultDrawable implements IDrawable {
    private final IAsset asset;

    DefaultDrawable(IAsset asset) {
        this.asset = asset;
    }

    @Override
    public void draw(GuiScreen gui, Point position, Point mousePosition) {
        gui.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        Rectangle area = asset.getArea();
        gui.drawTexturedModalRect(position.x, position.y, area.x, area.y, area.width, area.height);
    }
}

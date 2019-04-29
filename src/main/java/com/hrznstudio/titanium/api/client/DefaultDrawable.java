/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.client;

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

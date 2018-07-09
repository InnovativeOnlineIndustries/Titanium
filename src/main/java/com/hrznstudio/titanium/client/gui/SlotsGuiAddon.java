/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.inventory.PosInvHandler;

public class SlotsGuiAddon extends BasicGuiAddon {

    private final PosInvHandler handler;

    public SlotsGuiAddon(PosInvHandler handler) {
        super(handler.getXPos(), handler.getYPos());
        this.handler = handler;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiContainerTile container, float partialTicks, int mouseX, int mouseY) {
        container.mc.getTextureManager().bindTexture(container.getAssetProvider().getSlot().getResourceLocation());
        for (int x = 0; x < handler.getXSize(); x++) {
            for (int y = 0; y < handler.getYSize(); y++) {
                container.drawTexturedModalRect(handler.getXPos() + container.getAssetProvider().getSlot().getArea().width * x + container.getX() - 1,
                        handler.getYPos() + container.getAssetProvider().getSlot().getArea().height * y + container.getY() - 1,
                        container.getAssetProvider().getSlot().getArea().x,
                        container.getAssetProvider().getSlot().getArea().y,
                        container.getAssetProvider().getSlot().getArea().width,
                        container.getAssetProvider().getSlot().getArea().height);
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiContainerTile container, int mouseX, int mouseY) {

    }

    @Override
    public int getXSize() {
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }
}

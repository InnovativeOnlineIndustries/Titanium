/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.inventory.PosInvHandler;

import java.awt.*;

public class SlotsGuiAddon extends BasicGuiAddon {

    private final PosInvHandler handler;

    public SlotsGuiAddon(PosInvHandler handler) {
        super(handler.getXPos(), handler.getYPos());
        this.handler = handler;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiContainerTile container, float partialTicks, int mouseX, int mouseY) {
        IAsset slot = IAssetProvider.getAsset(container.getAssetProvider(), IAssetProvider.AssetType.SLOT);
        Rectangle area = slot.getArea();
        container.mc.getTextureManager().bindTexture(slot.getResourceLocation());
        for (int x = 0; x < handler.getXSize(); x++) {
            for (int y = 0; y < handler.getYSize(); y++) {
                container.drawTexturedModalRect(handler.getXPos() + area.width * x + container.getX() - 1,
                        handler.getYPos() + area.height * y + container.getY() - 1,
                        area.x,
                        area.y,
                        area.width,
                        area.height);
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
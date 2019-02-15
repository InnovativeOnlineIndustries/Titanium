/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.inventory.PosInvHandler;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class SlotsGuiAddon extends BasicGuiAddon {

    private final PosInvHandler handler;

    public SlotsGuiAddon(PosInvHandler handler) {
        super(handler.getXPos(), handler.getYPos());
        this.handler = handler;
    }

    @Override
    public int getXSize() {
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        IAsset slot = IAssetProvider.getAsset(provider, AssetTypes.SLOT);
        Point offset = slot.getOffset();
        Rectangle area = slot.getArea();
        screen.mc.getTextureManager().bindTexture(slot.getResourceLocation());
        for (int x = 0; x < handler.getXSize(); x++) {
            for (int y = 0; y < handler.getYSize(); y++) {
                screen.drawTexturedModalRect(handler.getXPos() + offset.x + area.width * x + guiX - 1,
                        handler.getYPos() + offset.y + area.height * y + guiY - 1,
                        area.x,
                        area.y,
                        area.width,
                        area.height);
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }
}
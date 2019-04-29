/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
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
        Rectangle area = slot.getArea();
        screen.mc.getTextureManager().bindTexture(slot.getResourceLocation());
        for (int x = 0; x < handler.getXSize(); x++) {
            for (int y = 0; y < handler.getYSize(); y++) {
                AssetUtil.drawAsset(screen, slot, handler.getXPos() + area.width * x + guiX - 1, handler.getYPos() + area.height * y + guiY - 1);
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }
}
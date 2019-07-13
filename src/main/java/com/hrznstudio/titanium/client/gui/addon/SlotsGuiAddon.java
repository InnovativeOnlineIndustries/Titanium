/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;

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
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        IAsset slot = IAssetProvider.getAsset(provider, AssetTypes.SLOT);
        Rectangle area = slot.getArea();
        screen.getMinecraft().getTextureManager().bindTexture(slot.getResourceLocation());
        for (int x = 0; x < handler.getXSize(); x++) {
            for (int y = 0; y < handler.getYSize(); y++) {
                if (handler instanceof SidedInvHandler && ((SidedInvHandler) handler).isColorGuiEnabled()) {
                    AbstractGui.fill(guiX + handler.getXPos() + area.width * x - 2, guiY + handler.getYPos() + area.height * y - 2,
                            guiX + handler.getXPos() + area.width * x + area.width, guiY + handler.getYPos() + area.height * y + area.height, ((SidedInvHandler) handler).getColor());
                    GlStateManager.color4f(1, 1, 1, 1);
                }
                AssetUtil.drawAsset(screen, slot, handler.getXPos() + area.width * x + guiX - 1, handler.getYPos() + area.height * y + guiY - 1);
                if (handler instanceof SidedInvHandler && ((SidedInvHandler) handler).isColorGuiEnabled()) {
                    Color color = new Color(((SidedInvHandler) handler).getColor());
                    AbstractGui.fill(guiX + handler.getXPos() + area.width * x, guiY + handler.getYPos() + area.height * y,
                            guiX + handler.getXPos() + area.width * x + area.width - 2, guiY + handler.getYPos() + area.height * y + area.height - 2, new Color(color.getRed(), color.getGreen(), color.getBlue(), 256 / 2).getRGB());
                    GlStateManager.color4f(1, 1, 1, 1);
                }
            }
        }

    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }
}
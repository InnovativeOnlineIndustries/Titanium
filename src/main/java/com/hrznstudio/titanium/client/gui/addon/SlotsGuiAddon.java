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
        //Draw background
        if (handler instanceof SidedInvHandler && ((SidedInvHandler) handler).isColorGuiEnabled()) {
            for (int slotID = 0; slotID < handler.getSlots(); slotID++) {
                int posX = handler.getSlotPosition().apply(slotID).getLeft();
                int posY = handler.getSlotPosition().apply(slotID).getRight();
                AbstractGui.fill(guiX + handler.getXPos() + posX - 2, guiY + handler.getYPos() + posY - 2,
                        guiX + handler.getXPos() + posX + area.width, guiY + handler.getYPos() + posY + area.height, ((SidedInvHandler) handler).getColor());
                GlStateManager.color4f(1, 1, 1, 1);
            }
        }
        //Draw slot
        for (int slotID = 0; slotID < handler.getSlots(); slotID++) {
            int posX = handler.getSlotPosition().apply(slotID).getLeft();
            int posY = handler.getSlotPosition().apply(slotID).getRight();
            AssetUtil.drawAsset(screen, slot, handler.getXPos() + posX + guiX - 1, handler.getYPos() + posY + guiY - 1);
        }
        //Draw overlay
        if (handler instanceof SidedInvHandler && ((SidedInvHandler) handler).isColorGuiEnabled()) {
            for (int slotID = 0; slotID < handler.getSlots(); slotID++) {
                int posX = handler.getSlotPosition().apply(slotID).getLeft();
                int posY = handler.getSlotPosition().apply(slotID).getRight();
                Color color = new Color(((SidedInvHandler) handler).getColor());
                AbstractGui.fill(guiX + handler.getXPos() + posX, guiY + handler.getYPos() + posY,
                        guiX + handler.getXPos() + posX + area.width - 2, guiY + handler.getYPos() + posY + area.height - 2, new Color(color.getRed(), color.getGreen(), color.getBlue(), 256 / 2).getRGB());
                GlStateManager.color4f(1, 1, 1, 1);
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }
}
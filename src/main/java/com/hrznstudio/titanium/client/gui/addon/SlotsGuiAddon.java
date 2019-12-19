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
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.function.Function;

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

    public static void drawAsset(Screen screen, IAssetProvider provider, int handlerPosX, int handlerPosY, int guiX, int guiY, int slots, Function<Integer, Pair<Integer, Integer>> positionFunction, boolean drawColor, int color) {
        IAsset slot = IAssetProvider.getAsset(provider, AssetTypes.SLOT);
        Rectangle area = slot.getArea();
        screen.getMinecraft().getTextureManager().bindTexture(slot.getResourceLocation());
        //Draw background
        if (drawColor) {
            for (int slotID = 0; slotID < slots; slotID++) {
                int posX = positionFunction.apply(slotID).getLeft();
                int posY = positionFunction.apply(slotID).getRight();
                AbstractGui.fill(guiX + handlerPosX + posX - 2, guiY + handlerPosY + posY - 2,
                        guiX + handlerPosX + posX + area.width, guiY + handlerPosY + posY + area.height, new Color(color).getRGB());
                RenderSystem.color4f(1, 1, 1, 1);
            }
        }
        //Draw slot
        for (int slotID = 0; slotID < slots; slotID++) {
            int posX = positionFunction.apply(slotID).getLeft();
            int posY = positionFunction.apply(slotID).getRight();
            AssetUtil.drawAsset(screen, slot, handlerPosX + posX + guiX - 1, handlerPosY + posY + guiY - 1);
        }
        //Draw overlay
        if (drawColor) {
            for (int slotID = 0; slotID < slots; slotID++) {
                int posX = positionFunction.apply(slotID).getLeft();
                int posY = positionFunction.apply(slotID).getRight();
                Color colored = new Color(color);
                AbstractGui.fill(guiX + handlerPosX + posX, guiY + handlerPosY + posY,
                        guiX + handlerPosX + posX + area.width - 2, guiY + handlerPosY + posY + area.height - 2, new Color(colored.getRed(), colored.getGreen(), colored.getBlue(), 256 / 2).getRGB());
                RenderSystem.color4f(1, 1, 1, 1);
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        drawAsset(screen, provider, guiX, guiY, getPosX(), getPosY(), handler.getSlots(), handler.getSlotPosition(), handler instanceof SidedInvHandler && ((SidedInvHandler) handler).isColorGuiEnabled(), handler instanceof SidedInvHandler ? ((SidedInvHandler) handler).getColor() : 0);
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }
}
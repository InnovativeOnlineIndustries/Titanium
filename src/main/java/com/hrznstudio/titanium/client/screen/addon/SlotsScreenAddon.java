/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SlotsScreenAddon<T extends IComponentHarness> extends BasicScreenAddon {

    private final InventoryComponent<T> handler;

    public SlotsScreenAddon(InventoryComponent<T> handler) {
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

    public static void drawAsset(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int handlerPosX, int handlerPosY, int guiX, int guiY, int slots, Function<Integer, Pair<Integer, Integer>> positionFunction, Function<Integer, ItemStack> slotToStackRenderMap, boolean drawColor, Function<Integer, Color> slotToColorRenderMap, Predicate<Integer> slotEnabled) {
        drawAsset(guiGraphics, screen, provider, handlerPosX, handlerPosY, guiX, guiY, slots,  positionFunction, slotToStackRenderMap, drawColor, slotToColorRenderMap, slotEnabled, 200);
    }

    public static void drawAsset(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int handlerPosX, int handlerPosY, int guiX, int guiY, int slots, Function<Integer, Pair<Integer, Integer>> positionFunction, Function<Integer, ItemStack> slotToStackRenderMap, boolean drawColor, Function<Integer, Color> slotToColorRenderMap, Predicate<Integer> slotEnabled, int overlayDepth) {
        IAsset slot = IAssetProvider.getAsset(provider, AssetTypes.SLOT);
        Rectangle area = slot.getArea();
        //RenderSystem.setShaderTexture(0, slot.getResourceLocation());
        //Draw background
        if (drawColor) {
            for (int slotID = 0; slotID < slots; slotID++) {
                if (!slotEnabled.test(slotID)) continue;
                int posX = positionFunction.apply(slotID).getLeft();
                int posY = positionFunction.apply(slotID).getRight();
                Color colored = slotToColorRenderMap.apply(slotID);
                if (colored != null) {
                    guiGraphics.fill(guiX + handlerPosX + posX - 2, guiY + handlerPosY + posY - 2,
                        guiX + handlerPosX + posX + area.width, guiY + handlerPosY + posY + area.height, new Color(colored.getRed(), colored.getGreen(), colored.getBlue(), 256 / 4).getRGB());
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                }
            }
        }
        //Draw slot
        for (int slotID = 0; slotID < slots; slotID++) {
            if (!slotEnabled.test(slotID)) continue;
            int posX = positionFunction.apply(slotID).getLeft();
            int posY = positionFunction.apply(slotID).getRight();
            AssetUtil.drawAsset(guiGraphics, screen, slot, handlerPosX + posX + guiX - 1, handlerPosY + posY + guiY - 1);
            //Draw ItemStack
            ItemStack stack1 = slotToStackRenderMap.apply(slotID);
            guiGraphics.renderItem(stack1, handlerPosX + posX + guiX, handlerPosY + posY + guiY);
            //RenderSystem.disableDepthTest();
        }
        RenderSystem.enableBlend();
        //Draw overlay
        if (drawColor) {
            for (int slotID = 0; slotID < slots; slotID++) {
                if (!slotEnabled.test(slotID)) continue;
                int posX = positionFunction.apply(slotID).getLeft();
                int posY = positionFunction.apply(slotID).getRight();
                Color colored = slotToColorRenderMap.apply(slotID);
                if (colored != null) {
                    guiGraphics.fill(RenderType.gui(), guiX + handlerPosX + posX, guiY + handlerPosY + posY,
                        guiX + handlerPosX + posX + area.width - 2, guiY + handlerPosY + posY + area.height - 2, overlayDepth, new Color(colored.getRed(), colored.getGreen(), colored.getBlue(), 256 / 2).getRGB());
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                }
            }
        }
        //RenderSystem.enableDepthTest();
    }

        @Override
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        drawAsset(guiGraphics, screen, provider, guiX, guiY, getPosX(), getPosY(), handler.getSlots(), handler.getSlotPosition(), handler::getItemStackForSlotRendering, handler.isColorGuiEnabled(), handler::getColorForSlotRendering, handler.getSlotVisiblePredicate());
    }

    @Override
    public void drawForegroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
    }

}

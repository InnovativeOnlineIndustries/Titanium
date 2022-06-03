/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.filter.FilterSlot;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.filter.ItemStackFilter;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Objects;

public class ItemstackFilterScreenAddon extends BasicScreenAddon {

    private final ItemStackFilter filter;

    public ItemstackFilterScreenAddon(ItemStackFilter filter) {
        super(filter.getFilterSlots()[0].getX(), filter.getFilterSlots()[0].getY());
        this.filter = filter;
    }

    @Override
    public int getXSize() {
        return filter.getFilterSlots()[filter.getFilterSlots().length - 1].getX() + 17;
    }

    @Override
    public int getYSize() {
        return filter.getFilterSlots()[filter.getFilterSlots().length - 1].getY() + 17;
    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        for (FilterSlot<ItemStack> filterSlot : filter.getFilterSlots()) {
            if (filterSlot != null) {
                Color color = new Color(filterSlot.getColor());
                AssetUtil.drawAsset(stack, screen, Objects.requireNonNull(provider.getAsset(AssetTypes.SLOT)), guiX + filterSlot.getX(), guiY + filterSlot.getY());
                GuiComponent.fill(stack, guiX + filterSlot.getX() + 1, guiY + filterSlot.getY() + 1,
                    guiX + filterSlot.getX() + 17, guiY + filterSlot.getY() + 17, new Color(color.getRed(), color.getGreen(), color.getBlue(), 256 / 2).getRGB());
                RenderSystem.setShaderColor(1, 1, 1, 1);
                if (!filterSlot.getFilter().isEmpty()) {
                    Lighting.setupFor3DItems(); //enableGUIStandarItemLightning
                    Minecraft.getInstance().getItemRenderer().renderGuiItem(filterSlot.getFilter(), filterSlot.getX() + guiX + 1, filterSlot.getY() + guiY + 1);
                }
            }
        }
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        for (FilterSlot<ItemStack> filterSlot : filter.getFilterSlots()) {
            if (filterSlot != null && mouseX > (guiX + filterSlot.getX() + 1) && mouseX < (guiX + filterSlot.getX() + 16) && mouseY > (guiY + filterSlot.getY() + 1) && mouseY < (guiY + filterSlot.getY() + 16)) {
                stack.translate(0, 0, 200);
                GuiComponent.fill(stack, filterSlot.getX() + 1, filterSlot.getY() + 1, filterSlot.getX() + 17, filterSlot.getY() + 17, -2130706433);
                stack.translate(0, 0, -200);
                if (!filterSlot.getFilter().isEmpty() && Minecraft.getInstance().player.containerMenu.getCarried().isEmpty()) {
                    screen.renderComponentTooltip(stack, screen.getTooltipFromItem(filterSlot.getFilter()), mouseX - guiX, mouseY - guiY);
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen && ((AbstractContainerScreen) screen).getMenu() instanceof ILocatable) {
            if (!isMouseOver(mouseX - ((AbstractContainerScreen<?>) screen).getGuiLeft(), mouseY - ((AbstractContainerScreen<?>) screen).getGuiTop()))
                return false;
            ILocatable locatable = (ILocatable) ((AbstractContainerScreen) screen).getMenu();
            for (FilterSlot<ItemStack> filterSlot : filter.getFilterSlots()) {
                if (filterSlot != null && mouseX > (((AbstractContainerScreen<?>) screen).getGuiLeft() + filterSlot.getX() + 1) && mouseX < (((AbstractContainerScreen<?>) screen).getGuiLeft() + filterSlot.getX() + 16) &&
                    mouseY > (((AbstractContainerScreen<?>) screen).getGuiTop() + filterSlot.getY() + 1) && mouseY < (((AbstractContainerScreen<?>) screen).getGuiTop() + filterSlot.getY() + 16)) {
                    CompoundTag compoundNBT = new CompoundTag();
                    compoundNBT.putString("Name", filter.getName());
                    compoundNBT.putInt("Slot", filterSlot.getFilterID());
                    compoundNBT.put("Filter", Minecraft.getInstance().player.containerMenu.getCarried().serializeNBT());
                    Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), -2, compoundNBT));
                    return true;
                }
            }
        }
        return false;
    }

}

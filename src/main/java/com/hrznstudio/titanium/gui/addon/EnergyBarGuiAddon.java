/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.gui.addon;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class EnergyBarGuiAddon extends BasicGuiAddon {

    private final IEnergyStorage handler;
    private IAsset background;

    public EnergyBarGuiAddon(int posX, int posY, IEnergyStorage handler) {
        super(posX, posY);
        this.handler = handler;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        background = IAssetProvider.getAsset(provider, IAssetProvider.AssetType.ENERGY_BAR);
        Point offset = background.getOffset();
        Rectangle area = background.getArea();
        screen.mc.getTextureManager().bindTexture(background.getResourceLocation());
        screen.drawTexturedModalRect(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        IAsset asset = IAssetProvider.getAsset(provider, IAssetProvider.AssetType.ENERGY_FILL);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        int stored = handler.getEnergyStored();
        int capacity = handler.getMaxEnergyStored();
        int powerOffset = stored * area.height / capacity;
        screen.drawTexturedModalRect(getPosX() + offset.x, getPosY() + offset.y + area.height - powerOffset, area.x, area.y + (area.height - powerOffset), area.width, powerOffset);
    }

    @Override
    public List<String> getTooltipLines() {
        return Arrays.asList(TextFormatting.AQUA + "Power:", new DecimalFormat().format(handler.getEnergyStored()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(handler.getMaxEnergyStored()));
    }

    @Override
    public int getXSize() {
        return background != null ? background.getArea().width : 0;
    }

    @Override
    public int getYSize() {
        return background != null ? background.getArea().height : 0;
    }
}
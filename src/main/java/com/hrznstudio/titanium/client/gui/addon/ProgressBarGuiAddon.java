/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ProgressBarGuiAddon extends BasicGuiAddon {

    private PosProgressBar progressBar;

    public ProgressBarGuiAddon(int posX, int posY, PosProgressBar posProgressBar) {
        super(posX, posY);
        this.progressBar = posProgressBar;
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
        IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        screen.drawTexturedModalRect(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
        GlStateManager.color4f(progressBar.getColor().getColorComponentValues()[0], progressBar.getColor().getColorComponentValues()[1], progressBar.getColor().getColorComponentValues()[2], 1);
        asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND);
        offset = asset.getOffset();
        area = asset.getArea();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        screen.drawTexturedModalRect(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        GlStateManager.color4f(progressBar.getColor().getColorComponentValues()[0], progressBar.getColor().getColorComponentValues()[1], progressBar.getColor().getColorComponentValues()[2], 1);
        IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR);
        progressBar.getBarDirection().render(screen, asset, this);
    }

    public PosProgressBar getProgressBar() {
        return progressBar;
    }
}

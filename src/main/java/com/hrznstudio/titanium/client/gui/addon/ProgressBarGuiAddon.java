/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;

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
    public void drawGuiContainerBackgroundLayer(GuiContainerTile container, float partialTicks, int mouseX, int mouseY) {
        IAsset asset = IAssetProvider.getAsset(container.getAssetProvider(), IAssetProvider.AssetType.PROGRESS_BAR);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        container.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        container.drawTexturedModalRect(container.getX() + getPosX() + offset.x, container.getY() + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiContainerTile container, int mouseX, int mouseY) {
        IAsset asset = IAssetProvider.getAsset(container.getAssetProvider(), IAssetProvider.AssetType.PROGRESS_BAR_FILL);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        container.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        int progress = progressBar.getProgress();
        int maxProgress = progressBar.getMaxProgress();
        int progressOffset = progress * area.width / maxProgress;
        container.drawTexturedModalRect(getPosX() + offset.x, getPosY() + offset.y, area.x, area.y, progressOffset, area.height);
    }
}

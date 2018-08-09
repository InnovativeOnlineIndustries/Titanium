/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.cassandra.client.gui.addon;

import com.hrznstudio.titanium.base.api.client.IAsset;
import com.hrznstudio.titanium.base.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.cassandra.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;

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
        IAsset asset = IAssetProvider.getAsset(provider, IAssetProvider.AssetType.PROGRESS_BAR);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        screen.drawTexturedModalRect(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        IAsset asset = IAssetProvider.getAsset(provider, IAssetProvider.AssetType.PROGRESS_BAR_FILL);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        int progress = progressBar.getProgress();
        int maxProgress = progressBar.getMaxProgress();
        int progressOffset = progress * area.width / maxProgress;
        screen.drawTexturedModalRect(getPosX() + offset.x, getPosY() + offset.y, area.x, area.y, progressOffset, area.height);
    }
}

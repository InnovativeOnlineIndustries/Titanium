/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;

public class AssetGuiAddon extends BasicGuiAddon {

    private IAssetType assetType;
    private boolean isBackground;
    private Rectangle area;

    public AssetGuiAddon(IAssetType assetType, int posX, int posY, boolean isBackground) {
        super(posX, posY);
        this.assetType = assetType;
        this.isBackground = isBackground;
    }

    @Override
    public int getXSize() {
        return area != null ? area.width : 0;
    }

    @Override
    public int getYSize() {
        return area != null ? area.height : 0;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        if (assetType != null) {
            IAsset asset = provider.getAsset(assetType);
            area = asset.getArea();
            AssetUtil.drawAsset(screen, asset, this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        if (assetType != null) {
            AssetUtil.drawAsset(screen, provider.getAsset(assetType), this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    public boolean isBackground() {
        return isBackground;
    }
}

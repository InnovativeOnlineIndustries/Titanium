/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.block.tile.button.ArrowButton;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.client.gui.screen.Screen;

public class ArrowButtonGuiAddon extends BasicButtonAddon {

    private ArrowButton button;
    private IAsset asset;

    public ArrowButtonGuiAddon(ArrowButton button) {
        super(button);
        this.button = button;
    }

    @Override
    public int getXSize() {
        return asset != null ? asset.getArea().width : 0;
    }

    @Override
    public int getYSize() {
        return asset != null ? asset.getArea().height : 0;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        asset = provider.getAsset(getAssetFromSideness(button.getDirection()));
        AssetUtil.drawAsset(screen, asset, guiX + getPosX(), guiY + getPosY());
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    public IAssetType getAssetFromSideness(FacingUtil.Sideness sideness) {
        switch (sideness) {
            case LEFT:
                return AssetTypes.BUTTON_ARROW_LEFT;
            case RIGHT:
                return AssetTypes.BUTTON_ARROW_RIGHT;
            case BOTTOM:
                return AssetTypes.BUTTON_ARROW_DOWN;
            default:
                return AssetTypes.BUTTON_ARROW_UP;
        }
    }
}

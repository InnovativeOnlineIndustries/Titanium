/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.button.ArrowButtonComponent;
import com.hrznstudio.titanium.util.AssetUtil;
import com.hrznstudio.titanium.util.FacingUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

public class ArrowButtonScreenAddon extends BasicButtonAddon {

    private ArrowButtonComponent button;
    private IAsset asset;

    public ArrowButtonScreenAddon(ArrowButtonComponent button) {
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
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        asset = provider.getAsset(getAssetFromSideness(button.getDirection()));
        AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX(), guiY + getPosY());
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {}

    public IAssetType<IAsset> getAssetFromSideness(FacingUtil.Sideness sideness) {
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

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.sideness;

import com.hrznstudio.titanium.api.client.IAssetType;

public class SidedHandlerManager {

    private final int posX;
    private final int posY;
    private final IAssetType assetType;

    public SidedHandlerManager(int posX, int posY, IAssetType assetType) {
        this.posX = posX;
        this.posY = posY;
        this.assetType = assetType;
    }

    public static SidedHandlerManager ofRight(int x, int y, int position, IAssetType managerIconType, int padding) {
        return new SidedHandlerManager(x + (managerIconType.getDefaultAsset().getArea().width + padding) * position, y, managerIconType);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public IAssetType getAssetType() {
        return assetType;
    }
}

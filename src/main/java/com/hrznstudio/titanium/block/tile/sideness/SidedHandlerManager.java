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

package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.tileentity.TileEntity;

public abstract class GuiTileAddonScreen extends GuiAddonScreen implements ITileContainer {

    private TileEntity tileEntity;

    public GuiTileAddonScreen(TileEntity tileEntity, IAssetProvider assetProvider, boolean drawBackground) {
        super(assetProvider, drawBackground);
        this.tileEntity = tileEntity;
    }

    @Override
    public TileEntity getTile() {
        return tileEntity;
    }
}

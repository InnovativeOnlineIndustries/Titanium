/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

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

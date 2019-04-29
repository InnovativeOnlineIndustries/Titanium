/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
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

/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.client.gui.asset;

import com.hrznstudio.titanium.api.client.IAsset;

import javax.annotation.Nonnull;
import java.awt.*;

public final class DefaultAssetProvider implements IAssetProvider {
    private final IAsset SLOT = () -> new Rectangle(1, 185, 18, 18);
    private final IAsset BACKGROUND = () -> new Rectangle(0, 0, 176, 184);
    private final IAsset ENERGY_BAR = () -> new Rectangle(211, 1, 11, 56);
    private final IAsset ENERGY_FILL = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(223, 1, 5, 50);
        }

        @Override
        public Point getOffset() {
            return new Point(3, 3);
        }
    };
    private final IAsset TANK = () -> new Rectangle(177, 1, 18, 46);
    private final Point HOTBAR_POS = new Point(8, 160);
    private final Point INV_POS = new Point(8, 102);
    private final IAsset PROGRESS_BAR = () -> new Rectangle(177, 61, 22, 15);
    private final IAsset PROGRESS_BAR_FILL = () -> new Rectangle(177, 77, 22, 16);

    DefaultAssetProvider() {
    }

    @Override
    public Point getInventoryPosition() {
        return INV_POS;
    }

    @Override
    public Point getHotbarPosition() {
        return HOTBAR_POS;
    }

    @Nonnull
    @Override
    public IAsset getAsset(AssetType assetType) {
        switch (assetType) {
            case BACKGROUND:
                return BACKGROUND;
            case SLOT:
                return SLOT;
            case TANK:
                return TANK;
            case ENERGY_BAR:
                return ENERGY_BAR;
            case ENERGY_FILL:
                return ENERGY_FILL;
            case PROGRESS_BAR:
                return PROGRESS_BAR;
            case PROGRESS_BAR_FILL:
                return PROGRESS_BAR_FILL;
        }
        throw new RuntimeException("An error has occurred, default provider encountered an unknown asset type");
    }
}
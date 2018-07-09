/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui.asset;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public final class DefaultAssetProvider implements IAssetProvider {
    DefaultAssetProvider() {
    }

    private final IAsset SLOT = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return DEFAULT_LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(1, 185, 18, 18);
        }
    };

    private final IAsset BACKGROUND = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return DEFAULT_LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 176, 184);
        }
    };
    private final IAsset ENERGY_BAR = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return DEFAULT_LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(211, 1, 11, 56);
        }
    };
    private final IAsset ENERGY_FILL = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return DEFAULT_LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(223, 1, 5, 50);
        }
    };
    private final IAsset TANK = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return DEFAULT_LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(177, 1, 18, 46);
        }
    };

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
        }
        throw new RuntimeException("An error has occurred, default provider encountered an unknown asset type");
    }
}
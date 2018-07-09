/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui;

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

    @Nonnull
    @Override
    public IAsset getAsset(AssetType assetType) {
        switch (assetType) {
            case BACKGROUND:
                return BACKGROUND;
            case SLOT:
                return SLOT;
        }
        throw new RuntimeException("An error has occurred, default provider encountered an unknown asset type");
    }
}
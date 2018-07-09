/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class DefaultAssetProvider implements IAssetProvider {
    @Nonnull
    @Override
    public IAsset getAsset(AssetType assetType) {
        switch (assetType) {
            case BACKGROUND:
                return new IAsset() {
                    @Override
                    public ResourceLocation getResourceLocation() {
                        return DEFAULT_LOCATION;
                    }

                    @Override
                    public Rectangle getArea() {
                        return new Rectangle(0, 0, 176, 184);
                    }
                };
            case SLOT:
                return new IAsset() {
                    @Override
                    public ResourceLocation getResourceLocation() {
                        return DEFAULT_LOCATION;
                    }

                    @Override
                    public Rectangle getArea() {
                        return new Rectangle(1, 185, 18, 18);
                    }
                };
        }
        throw new RuntimeException("An error has occurred, default provider encountered an unknown asset type");
    }
}
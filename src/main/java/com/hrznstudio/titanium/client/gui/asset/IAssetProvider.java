/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.client.gui.asset;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public interface IAssetProvider {
    ResourceLocation DEFAULT_LOCATION = new ResourceLocation(Titanium.MODID, "textures/gui/background.png");

    DefaultAssetProvider DEFAULT_PROVIDER = new DefaultAssetProvider();

    @Nonnull
    static IAsset getAsset(IAssetProvider provider, AssetType type) {
        if (provider == DEFAULT_PROVIDER)
            return DEFAULT_PROVIDER.getAsset(type);
        IAsset asset = provider.getAsset(type);
        return asset != null ? asset : DEFAULT_PROVIDER.getAsset(type);
    }

    Point getInventoryPosition();

    Point getHotbarPosition();

    /**
     * Provide custom assets to
     *
     * @param assetType The asset type requested
     * @return The IAsset requested, if you don't wish to have a custom asset, return null
     */
    @Nullable
    IAsset getAsset(AssetType assetType);

    enum AssetType {
        BACKGROUND,
        SLOT,
        ENERGY_BAR,
        ENERGY_FILL,
        TANK,
        PROGRESS_BAR,
        PROGRESS_BAR_FILL;
    }
}
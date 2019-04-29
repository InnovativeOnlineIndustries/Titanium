/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.client.gui.asset;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAssetProvider {
    ResourceLocation DEFAULT_LOCATION = new ResourceLocation(Titanium.MODID, "textures/gui/background.png");

    DefaultAssetProvider DEFAULT_PROVIDER = new DefaultAssetProvider();

    @Nonnull
    static <T extends IAsset> T getAsset(IAssetProvider provider, IAssetType<T> type) {
        T asset = provider.getAsset(type);
        if (asset == null && provider != DEFAULT_PROVIDER)
            asset = DEFAULT_PROVIDER.getAsset(type);
        return asset != null ? asset : type.getDefaultAsset();
    }

    /**
     * Provide custom assets to
     *
     * @param assetType The assets type requested
     * @return The IAsset requested, if you don't wish to have a custom assets, return null
     */
    @Nullable
    <T extends IAsset> T getAsset(IAssetType<T> assetType);

}
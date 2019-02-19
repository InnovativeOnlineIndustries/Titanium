/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;

public class AssetTypes {
    public static final IAssetType<IAsset> PROGRESS_BAR_BORDER = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_BACKGROUND = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> ENERGY_BACKGROUND = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> ENERGY_BAR = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> SLOT = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IBackgroundAsset> BACKGROUND = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IBackgroundAsset.class);
    public static final IAssetType<ITankAsset> TANK = new GenericAssetType<ITankAsset>(IAssetProvider.DEFAULT_PROVIDER::getAsset, ITankAsset.class);
}

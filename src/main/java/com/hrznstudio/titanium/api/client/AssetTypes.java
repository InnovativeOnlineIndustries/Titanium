/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;

public class AssetTypes {
    public static final IAssetType<IAsset> BUTTON_SIDENESS_DISABLED = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_SIDENESS_ENABLED = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_SIDENESS_PULL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_SIDENESS_PUSH = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<ITankAsset> TANK_NORMAL = new GenericAssetType<ITankAsset>(IAssetProvider.DEFAULT_PROVIDER::getAsset, ITankAsset.class);
    public static final IAssetType<ITankAsset> TANK_SMALL = new GenericAssetType<ITankAsset>(IAssetProvider.DEFAULT_PROVIDER::getAsset, ITankAsset.class);
    public static final IAssetType<IBackgroundAsset> BACKGROUND = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IBackgroundAsset.class);
    public static final IAssetType<IAsset> SLOT = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> ENERGY_BAR = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> ENERGY_BACKGROUND = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_BORDER_VERTICAL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_VERTICAL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_BACKGROUND_VERTICAL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_BACKGROUND_ARROW_DOWN = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_ARROW_DOWN = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_SIDENESS_MANAGER = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> AUGMENT_BACKGROUND = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> AUGMENT_BACKGROUND_LEFT = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> AUGMENT_BACKGROUND_LEFT_TALL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_ARROW_LEFT = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_ARROW_RIGHT = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_ARROW_UP = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_ARROW_DOWN = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> ITEM_BACKGROUND = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> TEXT_FIELD_ACTIVE = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> TEXT_FIELD_INACTIVE = new GenericAssetType<>(IAssetType::getDefaultAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_REDSTONE_ONCE = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_REDSTONE_REDSTONE = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_REDSTONE_NO_REDSTONE = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> BUTTON_REDSTONE_IGNORED = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> PROGRESS_BAR_ARROW_HORIZONTAL = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> SHADE_PICKER = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> HUE_PICKER = new GenericAssetType<>(IAssetProvider.DEFAULT_PROVIDER::getAsset, IAsset.class);
}

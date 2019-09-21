/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.assetsystem;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TestAssetProvider implements IAssetProvider {
    private static Map<IAssetType, IAsset> assetMap;
    private static ResourceLocation TEST_LOCATION = new ResourceLocation(Titanium.MODID, "textures/gui/carpenter_dev.png");

    private static final IBackgroundAsset BACKGROUND_ASSET = new IBackgroundAsset() {
        @Override
        public Point getInventoryPosition() {
            return new Point(8, 136);
        }

        @Override
        public Point getHotbarPosition() {
            return new Point(8, 194);
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 176, 218);
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return TEST_LOCATION;
        }
    };

    private static final IAsset PROGRESS_BAR_EMPTY = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(176, 77, 4, 18);
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return TEST_LOCATION;
        }
    };

    private static final IAsset PROGRESS_BAR_FILL = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(176, 60, 4, 16);
        }

        @Override
        public Point getOffset() {
            return new Point(0, 1);
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return TEST_LOCATION;
        }
    };

    public static final ITankAsset TANK = new ITankAsset() {
        @Override
        public int getFluidRenderPadding(Direction facing) {
            return 3;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(176, 0, 16, 60);
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return TEST_LOCATION;
        }
    };

    public TestAssetProvider() {
        assetMap = new HashMap<>();
        assetMap.put(AssetTypes.BACKGROUND, BACKGROUND_ASSET);
        assetMap.put(AssetTypes.PROGRESS_BAR_VERTICAL, PROGRESS_BAR_FILL);
        assetMap.put(AssetTypes.PROGRESS_BAR_BORDER_VERTICAL, PROGRESS_BAR_EMPTY);
        assetMap.put(AssetTypes.PROGRESS_BAR_BACKGROUND_VERTICAL, PROGRESS_BAR_EMPTY);
        assetMap.put(AssetTypes.TANK_NORMAL, TANK);
    }

    @Nullable
    @Override
    public <T extends IAsset> T getAsset(IAssetType<T> assetType) {
        if (assetMap.containsKey(assetType)) {
            return assetType.castOrDefault(assetMap.get(assetType));
        }
        return assetType.getDefaultAsset();
    }
}

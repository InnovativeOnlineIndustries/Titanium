/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.sideness;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.screen.addon.StateButtonInfo;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Map;

public interface IFacingComponent {
    Map<FacingUtil.Sideness, FaceMode> getFacingModes();

    int getColor();

    Rectangle getRectangle(IAsset asset);

    String getName();

    int getFacingHandlerX();

    int getFacingHandlerY();

    boolean work(World world, BlockPos pos, Direction blockFacing, int workAmount);

    IFacingComponent setFacingHandlerPos(int x, int y);

    enum FaceMode {
        NONE(false, 0, AssetTypes.BUTTON_SIDENESS_DISABLED, TextFormatting.RED),
        ENABLED(true, 1, AssetTypes.BUTTON_SIDENESS_ENABLED, TextFormatting.GREEN),
        PUSH(true, 2, AssetTypes.BUTTON_SIDENESS_PUSH, TextFormatting.YELLOW),
        PULL(true, 3, AssetTypes.BUTTON_SIDENESS_PULL, TextFormatting.BLUE);

        private final boolean allowsConnection;
        private final int index;
        private final IAssetType<?> assetType;
        private final TextFormatting color;

        FaceMode(boolean allowsConnection, int index, IAssetType<?> assetType, TextFormatting color) {
            this.allowsConnection = allowsConnection;
            this.index = index;
            this.assetType = assetType;
            this.color = color;
        }

        public boolean allowsConnection() {
            return allowsConnection;
        }

        public StateButtonInfo getInfo() {
            return new StateButtonInfo(index, assetType, "tooltip.titanium.facing_handler." + this.name().toLowerCase());
        }

        public int getIndex() {
            return index;
        }

        public TextFormatting getColor() {
            return color;
        }
    }
}

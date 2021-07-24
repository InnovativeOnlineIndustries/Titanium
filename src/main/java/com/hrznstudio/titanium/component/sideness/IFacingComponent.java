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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.Map;

public interface IFacingComponent {

    Map<FacingUtil.Sideness, FaceMode> getFacingModes();

    int getColor();

    Rectangle getRectangle(IAsset asset);

    String getName();

    int getFacingHandlerX();

    int getFacingHandlerY();

    boolean work(Level world, BlockPos pos, Direction blockFacing, int workAmount);

    IFacingComponent setFacingHandlerPos(int x, int y);

    FaceMode[] getValidFacingModes();

    enum FaceMode {
        NONE(false, 0, AssetTypes.BUTTON_SIDENESS_DISABLED, ChatFormatting.RED),
        ENABLED(true, 1, AssetTypes.BUTTON_SIDENESS_ENABLED, ChatFormatting.GREEN),
        PUSH(true, 2, AssetTypes.BUTTON_SIDENESS_PUSH, ChatFormatting.YELLOW),
        PULL(true, 3, AssetTypes.BUTTON_SIDENESS_PULL, ChatFormatting.BLUE);

        private final boolean allowsConnection;
        private final int index;
        private final IAssetType<?> assetType;
        private final ChatFormatting color;

        FaceMode(boolean allowsConnection, int index, IAssetType<?> assetType, ChatFormatting color) {
            this.allowsConnection = allowsConnection;
            this.index = index;
            this.assetType = assetType;
            this.color = color;
        }

        public boolean allowsConnection() {
            return allowsConnection;
        }

        public StateButtonInfo getInfo(int index) {
            return new StateButtonInfo(index, assetType, "tooltip.titanium.facing_handler." + this.name().toLowerCase());
        }

        public int getIndex() {
            return index;
        }

        public ChatFormatting getColor() {
            return color;
        }
    }
}

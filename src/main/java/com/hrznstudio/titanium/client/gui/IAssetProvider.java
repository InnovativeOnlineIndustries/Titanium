/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public interface IAssetProvider {
    ResourceLocation DEFAULT_LOCATION = new ResourceLocation(Titanium.MODID, "textures/gui/background.png");
    IAssetProvider DEFAULT_PROVIDER = new IAssetProvider() {
    };

    default IAsset getBackground() {
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
    }

    default IAsset getSlot() {
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

}
/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public interface IAsset {

    default ResourceLocation getResourceLocation() {
        return IAssetProvider.DEFAULT_LOCATION;
    }

    Rectangle getArea();

    default Point getOffset() {
        return new Point();
    }

}

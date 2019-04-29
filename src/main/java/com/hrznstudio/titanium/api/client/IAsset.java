/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.util.ResourceLocation;

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
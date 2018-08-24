/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.api.client;

import com.hrznstudio.titanium.cassandra.client.gui.asset.IAssetProvider;
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
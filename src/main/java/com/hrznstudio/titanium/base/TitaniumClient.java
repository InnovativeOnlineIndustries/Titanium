/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base;

import com.hrznstudio.titanium.base.client.TitaniumModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class TitaniumClient {
    public static void registerModelLoader() {
        ModelLoaderRegistry.registerLoader(new TitaniumModelLoader());
    }
}

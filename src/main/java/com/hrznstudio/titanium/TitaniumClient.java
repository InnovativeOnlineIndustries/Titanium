/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium.client.TitaniumModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class TitaniumClient {
    public static void registerModelLoader() {
        ModelLoaderRegistry.registerLoader(new TitaniumModelLoader());
    }

    public static PlayerRenderer getPlayerRenderer(Minecraft minecraft) {
        return minecraft.getRenderManager().playerRenderer;
    }
}

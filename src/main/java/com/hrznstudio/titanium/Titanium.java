/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium;

import com.hrznstudio.titanium.client.TitaniumModelLoader;
import com.hrznstudio.titanium.util.SidedHandler;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Titanium.MODID, name = Titanium.NAME, version = Titanium.VERSION)
public class Titanium {
    public static final String MODID = "titanium";
    public static final String NAME = "Titanium";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SidedHandler.runOn(Side.CLIENT, () -> () -> ModelLoaderRegistry.registerLoader(new TitaniumModelLoader()));
    }
}

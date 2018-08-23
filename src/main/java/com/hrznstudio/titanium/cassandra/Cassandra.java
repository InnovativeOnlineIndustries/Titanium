/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.cassandra;

import com.google.common.eventbus.Subscribe;
import com.hrznstudio.titanium.base.Titanium;
import com.hrznstudio.titanium.base.pulsar.pulse.Pulse;
import com.hrznstudio.titanium.cassandra.client.gui.GuiHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Pulse(id = "cassandra")
public class Cassandra {

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Titanium.INSTANCE, new GuiHandler());
    }
}

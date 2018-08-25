/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
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

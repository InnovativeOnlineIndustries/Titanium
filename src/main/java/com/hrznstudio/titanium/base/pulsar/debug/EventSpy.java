/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.pulsar.debug;

import com.google.common.eventbus.Subscribe;
import com.hrznstudio.titanium.base.pulsar.pulse.Pulse;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Debug Pulse to 'eavesdrop' on the PulseManager event bus traffic.
 *
 * @author Arkan <arkan@drakon.io>
 */
@SuppressWarnings("unused")
@Pulse(id = "EventSpy", description = "we iz in ur buses, monitorin ur eventz", forced = true)
public class EventSpy {

    private final Logger log = LogManager.getLogger("EventSpy/" + Loader.instance().activeModContainer().getModId());

    @Subscribe
    public void receive(Object evt) {
        log.info("Received event: " + evt);
    }

}

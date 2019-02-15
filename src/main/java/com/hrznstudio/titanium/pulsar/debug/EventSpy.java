/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.pulsar.debug;

import com.google.common.eventbus.Subscribe;
import com.hrznstudio.titanium.pulsar.pulse.Pulse;
import net.minecraftforge.fml.ModLoadingContext;
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

    private final Logger log = LogManager.getLogger("EventSpy/" + ModLoadingContext.get().getActiveContainer().getModId());

    @Subscribe
    public void receive(Object evt) {
        log.info("Received event: " + evt);
    }

}
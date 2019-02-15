/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.pulsar.internal;

import com.hrznstudio.titanium.pulsar.flightpath.IExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Custom exception catcher that logs events.
 */
@ParametersAreNonnullByDefault
public final class BusExceptionHandler implements IExceptionHandler {

    private final String id;
    private final Logger logger;

    /**
     * @param id Mod ID to include in exception raises.
     */
    public BusExceptionHandler(String id) {
        this.id = id;
        this.logger = LogManager.getLogger(id + "-Pulsar-Flightpath");
    }

    @Override
    public void handle(Exception ex) {
        this.logger.error("Exception caught from a pulse on flightpath for mod ID " + id, ex);
    }

    @Override
    public void flush() {
        // NO-OP
    }

}

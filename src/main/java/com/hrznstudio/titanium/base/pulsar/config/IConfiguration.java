/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.pulsar.config;

import com.hrznstudio.titanium.base.pulsar.pulse.PulseMeta;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interface for config handlers.
 *
 * @author Arkan <arkan@drakon.io>
 */
@ParametersAreNonnullByDefault
public interface IConfiguration {

    /**
     * Perform any configuration loading required.
     */
    public void load();

    /**
     * Gets whether the given module is enabled in the config.
     *
     * @param meta The pulse metadata.
     * @return Whether the module is enabled.
     */
    public boolean isModuleEnabled(PulseMeta meta);

    /**
     * Flush configuration to disk/database/whatever.
     */
    public void flush();

}

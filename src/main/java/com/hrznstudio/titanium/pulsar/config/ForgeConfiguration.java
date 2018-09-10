/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.pulsar.config;

import com.hrznstudio.titanium.pulsar.pulse.PulseMeta;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.Locale;

public class ForgeConfiguration implements IConfiguration {

    private final String confPath;
    private final String description;
    private Configuration config;

    public ForgeConfiguration(String confName, String description) {
        this.confPath = Loader.instance().getConfigDir().toString() + File.separator + confName + ".cfg";
        this.description = description.toLowerCase(Locale.ENGLISH);
    }

    public ForgeConfiguration(String confName) {
        this(confName, "Modules");
    }

    @Override
    public void load() {
        if (config == null)
            config = new Configuration(new File(this.confPath), "1");
        config.load();
    }

    @Override
    public boolean isModuleEnabled(PulseMeta meta) {
        Property prop = config.get(this.description, meta.getId(), meta.isDefaultEnabled(), meta.getDescription());
        prop.setRequiresMcRestart(true);
        return prop.getBoolean(meta.isEnabled());
    }

    @Override
    public void flush() {
        if (config.hasChanged()) {
            config.save();
        }
    }
}

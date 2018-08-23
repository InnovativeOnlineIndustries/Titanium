/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.pulsar.internal;

import com.hrznstudio.titanium.base.pulsar.control.PulseManager;
import com.hrznstudio.titanium.base.pulsar.pulse.PulseMeta;
import net.minecraftforge.fml.common.ICrashCallable;

/**
 * FML crash callable for Pulse Managers; dumps a list of loaded pulses to the error log.
 *
 * @author Arkan <arkan@drakon.io>
 */
public class CrashHandler implements ICrashCallable {

    private String id;
    private PulseManager manager;

    public CrashHandler(String modId, PulseManager manager) {
        this.id = "Pulsar/" + modId + " loaded Pulses";
        this.manager = manager;
    }

    private static String getStateFromMeta(PulseMeta meta) {
        if (meta.isForced()) {
            return "Enabled/Forced";
        } else {
            if (meta.isEnabled()) {
                return "Enabled/Not Forced";
            } else {
                return "Disabled/Not Forced";
            }
        }
    }

    @Override
    public String getLabel() {
        return id;
    }

    @Override
    public String call() {
        StringBuilder out = new StringBuilder("\n");
        for (PulseMeta meta : manager.getAllPulseMetadata()) {
            String state = getStateFromMeta(meta);
            out.append("\t\t- ").append(meta.getId()).append(" (").append(state).append(")\n"); // Yes, yes, manual indenting, I know...
        }
        return out.toString();
    }

}

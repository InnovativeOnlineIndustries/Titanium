/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.pulsar.internal;

import com.hrznstudio.titanium.pulsar.control.PulseManager;
import com.hrznstudio.titanium.pulsar.pulse.PulseMeta;
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

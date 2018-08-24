/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.pulsar.pulse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Metadata wrapper for parsed @Pulse metadata.
 *
 * @author Arkan <arkan@drakon.io>
 */
@ParametersAreNonnullByDefault
public class PulseMeta {

    private final String id, description;
    private final boolean forced, defaultEnabled;
    private boolean enabled;
    private boolean missingDeps = false;

    public PulseMeta(String id, @Nullable String description, boolean forced, boolean enabled, boolean defaultEnabled) {
        this.id = id;
        this.description = description;
        this.forced = forced;
        this.enabled = enabled;
        this.defaultEnabled = defaultEnabled;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isForced() {
        return !missingDeps && forced;
    }

    public boolean isEnabled() {
        return !missingDeps && enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMissingDeps(boolean missing) {
        missingDeps = missing;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }
}

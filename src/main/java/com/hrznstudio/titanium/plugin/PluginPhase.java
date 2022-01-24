/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.plugin;

public enum PluginPhase {
    CONSTRUCTION,
    CLIENT_SETUP,
    COMMON_SETUP,
    PRE_INIT,
    INIT,
    POST_INIT,
    CONFIG_LOAD,
    CONFIG_RELOAD
}

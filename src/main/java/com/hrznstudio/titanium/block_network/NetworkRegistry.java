/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block_network;


import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class NetworkRegistry {
    public static final NetworkRegistry INSTANCE = new NetworkRegistry();
    private static final Logger LOGGER = LogManager.getLogger(NetworkRegistry.class);
    private final Map<ResourceLocation, NetworkFactory> factories = new HashMap<>();

    private NetworkRegistry() {
    }

    public void addFactory(ResourceLocation type, NetworkFactory factory) {
        if (factories.containsKey(type)) {
            throw new RuntimeException("Cannot register duplicate network type " + type.toString());
        }

        LOGGER.debug("Registering network factory {}", type.toString());

        factories.put(type, factory);
    }

    @Nullable
    public NetworkFactory getFactory(ResourceLocation type) {
        return factories.get(type);
    }
}

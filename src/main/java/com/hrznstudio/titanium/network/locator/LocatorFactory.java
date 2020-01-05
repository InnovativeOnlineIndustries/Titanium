/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator;

import com.google.common.collect.Maps;
import com.hrznstudio.titanium.Titanium;
import net.minecraft.network.PacketBuffer;

import java.util.Map;

public class LocatorFactory {
    private final static Map<String, LocatorType> LOCATOR_TYPES = Maps.newHashMap();

    public static void registerLocatorType(LocatorType locatorType) {
        if (!LOCATOR_TYPES.containsKey(locatorType.getName())) {
            LOCATOR_TYPES.put(locatorType.getName(), locatorType);
        } else {
            Titanium.LOGGER.error("Locator Type already registered for name {}", locatorType.getName());
        }
    }

    public static LocatorInstance readPacketBuffer(PacketBuffer packetBuffer) {
        String name = packetBuffer.readString(64);
        LocatorType type = LOCATOR_TYPES.get(name);
        if (type != null) {
            LocatorInstance instance = type.createInstance();
            instance.fromBytes(packetBuffer);
            return instance;
        } else {
            return null;
        }
    }

    public static void writePacketBuffer(PacketBuffer buffer, LocatorInstance instance) {
        buffer.writeString(instance.getType().getName(), 64);
        instance.toBytes(buffer);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.network;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;

public class NetworkHandler {
    public static SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Titanium.MODID, "network"),
            () -> "1.0",
            s -> true,
            s -> true
    );
    private static int i = 0;

    public static <REQ extends Message> void registerMessage(Class<REQ> message) {
        NETWORK.registerMessage(i++, message, Message::toBytes,
                buffer -> {
                    try {
                        REQ req = message.getConstructor().newInstance();
                        req.fromBytes(buffer);
                        return req;
                    } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                },
                (req, contextSupplier) -> req.handleMessage(contextSupplier.get()));
    }
}
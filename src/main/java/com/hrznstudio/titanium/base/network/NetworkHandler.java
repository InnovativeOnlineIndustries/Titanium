/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.network;

import com.hrznstudio.titanium.base.Titanium;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Titanium.MODID);
    private static int i = 0;

    public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handler, Class<REQ> message, Side side) {
        NETWORK.registerMessage(handler, message, i++, side);
    }

    public static <REQ extends Message<REQ>> void registerMessage(Class<REQ> message, Side side) {
        registerMessage(message, message, side);
    }
}
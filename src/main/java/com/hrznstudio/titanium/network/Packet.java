/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.network;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class Packet {
    public Packet() {
    }

    public Packet(PacketBuffer buffer) {
        this();
        decode(buffer);
    }

    public abstract void encode(PacketBuffer buffer);

    public abstract void decode(PacketBuffer buffer);

    public abstract void handleMessage(Supplier<NetworkEvent.Context> context);
}

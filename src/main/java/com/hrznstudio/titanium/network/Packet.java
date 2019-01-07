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

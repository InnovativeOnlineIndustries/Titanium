package com.hrznstudio.titanium.network;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenGUI extends Packet {
    private int x;
    private int y;
    private int z;

    public OpenGUI() {
    }

    public OpenGUI(PacketBuffer buffer) {
        super(buffer);
    }

    public OpenGUI(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        x=buffer.readInt();
        y=buffer.readInt();
        z=buffer.readInt();
    }

    @Override
    public void handleMessage(Supplier<NetworkEvent.Context> context) {
        Minecraft.getInstance().displayGuiScreen((GuiScreen) Titanium.guiHandler.getClientGuiElement(0, Minecraft.getInstance().player, Minecraft.getInstance().world, x, y, z));
    }
}

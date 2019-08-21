package com.hrznstudio.titanium.client.gui.addon.interfaces;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public interface INetworkable {
    void sendMessage(int id, CompoundNBT data);
}

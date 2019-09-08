package com.hrznstudio.titanium.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IButtonHandler {
    void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound);
}

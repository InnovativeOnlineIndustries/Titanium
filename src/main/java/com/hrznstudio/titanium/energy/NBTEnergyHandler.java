/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.energy;

import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class NBTEnergyHandler extends EnergyStorage implements INBTSerializable<NBTTagInt> {
    public NBTEnergyHandler(int capacity) {
        super(capacity);
    }

    public NBTEnergyHandler(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public NBTEnergyHandler(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public NBTEnergyHandler(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public NBTTagInt serializeNBT() {
        return new NBTTagInt(getEnergyStored());
    }

    @Override
    public void deserializeNBT(NBTTagInt nbt) {
        energy = nbt.getInt();
    }
}
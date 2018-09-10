/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.energy;

import com.hrznstudio.titanium.block.tile.TileBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class NBTEnergyHandler extends EnergyStorage implements INBTSerializable<NBTTagInt> {

    private TileBase base;

    public NBTEnergyHandler(TileBase base, int capacity) {
        super(capacity);
        this.base = base;
    }

    public NBTEnergyHandler(TileBase base, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.base = base;
    }

    public NBTEnergyHandler(TileBase base, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.base = base;
    }

    public NBTEnergyHandler(TileBase base, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.base = base;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        if (energy != 0 && !simulate) base.markForUpdate();
        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        if (energy != 0 && !simulate) base.markForUpdate();
        return energy;
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
/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.energy;

import com.hrznstudio.titanium.block.tile.BasicTile;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class NBTEnergyHandler extends EnergyStorage implements INBTSerializable<IntNBT> {

    private BasicTile base;

    public NBTEnergyHandler(BasicTile base, int capacity) {
        super(capacity);
        this.base = base;
    }

    public NBTEnergyHandler(BasicTile base, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.base = base;
    }

    public NBTEnergyHandler(BasicTile base, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.base = base;
    }

    public NBTEnergyHandler(BasicTile base, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.base = base;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        if (energy != 0 && !simulate) {
            base.markForUpdate();
        }
        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        if (energy != 0 && !simulate) {
            base.markForUpdate();
        }
        return energy;
    }

    public void receiveEnergyForced(int maxReceive) {
        this.energy = Math.min(this.capacity, this.energy + maxReceive);
        base.markForUpdate();
    }

    public void extractEnergyForced(int maxExtract) {
        this.energy = Math.max(0, this.energy - maxExtract);
        base.markForUpdate();
    }

    @Override
    public IntNBT serializeNBT() {
        return IntNBT.of(getEnergyStored());
    }

    @Override
    public void deserializeNBT(IntNBT nbt) {
        energy = nbt.getInt();
    }
}
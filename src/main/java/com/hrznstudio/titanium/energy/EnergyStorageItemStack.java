/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.energy;

import com.hrznstudio.titanium.attachment.StoredEnergyAttachment;
import com.hrznstudio.titanium.item.EnergyItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyStorageItemStack implements IEnergyStorage {
    private final ItemStack stack;

    public EnergyStorageItemStack(ItemStack stack) {
        this.stack = stack;
    }

    public void putInternal(int energy) {
        save(new StoredEnergyAttachment(Math.min(getEnergyStored() + energy, getMaxEnergyStored()), getMaxEnergyStored(), getMaxReceive(), getMaxExtract()));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxReceive(), maxReceive));

        if (!simulate) {
            if (energyReceived != 0) {
                save(new StoredEnergyAttachment(getEnergyStored() + energyReceived, getMaxEnergyStored(), getMaxReceive(), getMaxExtract()));
            }
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxExtract(), maxExtract));

        if (!simulate) {
            if (stack != null && energyExtracted != 0) {
                save(new StoredEnergyAttachment(getEnergyStored() - energyExtracted, getMaxEnergyStored(), getMaxReceive(), getMaxExtract()));
            }
        }
        return energyExtracted;
    }

    public int getMaxExtract() {
        return get().out();
    }

    public int getMaxReceive() {
        return get().in();
    }

    @Override
    public int getEnergyStored() {
        return get().stored();
    }

    @Override
    public int getMaxEnergyStored() {
        return get().capacity();
    }

    @Override
    public boolean canExtract() {
        return getMaxExtract() > 0;
    }

    @Override
    public boolean canReceive() {
        return getMaxReceive() > 0;
    }

    public void save(StoredEnergyAttachment attachment) {
        stack.set(StoredEnergyAttachment.TYPE, attachment);
    }

    public StoredEnergyAttachment get() {
        return stack.getOrDefault(StoredEnergyAttachment.TYPE, new StoredEnergyAttachment((EnergyItem) stack.getItem()));
    }
}

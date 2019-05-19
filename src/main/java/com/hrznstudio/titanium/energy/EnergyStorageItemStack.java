/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageItemStack implements IEnergyStorage {
    private static final String ENERGY = "stored";
    private static final String MAX = "max";
    private static final String INPUT = "in";
    private static final String OUTPUT = "out";
    private final ItemStack stack;

    public EnergyStorageItemStack(ItemStack stack, int capacity, int in, int out) {
        this.stack = stack;
        boolean hasTags = stack.hasTag();
        if (!hasTags || !stack.getTag().contains("energy")) {
            if (!hasTags) {
                stack.setTag(new NBTTagCompound());
            }
            NBTTagCompound tag = stack.getTag();
            NBTTagCompound energyTag = new NBTTagCompound();
            energyTag.putInt(ENERGY, 0);
            energyTag.putInt(MAX, capacity);
            energyTag.putInt(INPUT, in);
            energyTag.putInt(OUTPUT, out);
            tag.put("energy", energyTag);
        } else {
            NBTTagCompound energyTag = getStackEnergyTag();
            energyTag.putInt(MAX, capacity);
            energyTag.putInt(INPUT, in);
            energyTag.putInt(OUTPUT, out);
        }
    }

    public void putInternal(int energy) {
        NBTTagCompound energyTag = getStackEnergyTag();
        energyTag.putInt(ENERGY, Math.min(energyTag.getInt(ENERGY) + energy, energyTag.getInt(MAX)));
    }

    private NBTTagCompound getStackEnergyTag() {
        return stack.getChildTag("energy");
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxReceive(), maxReceive));

        if (!simulate) {
            if (energyReceived != 0) {
                getStackEnergyTag().putInt("energy", getEnergyStored() + energyReceived);
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
                getStackEnergyTag().putInt("energy", getEnergyStored() - energyExtracted);
            }
        }
        return energyExtracted;
    }

    public int getMaxExtract() {
        return getStackEnergyTag().getInt(OUTPUT);
    }

    public int getMaxReceive() {
        return getStackEnergyTag().getInt(INPUT);
    }

    @Override
    public int getEnergyStored() {
        return getStackEnergyTag().getInt(ENERGY);
    }

    @Override
    public int getMaxEnergyStored() {
        return getStackEnergyTag().getInt(MAX);
    }

    @Override
    public boolean canExtract() {
        return getMaxExtract() > 0;
    }

    @Override
    public boolean canReceive() {
        return getMaxReceive() > 0;
    }
}
/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.block.tile;

import com.google.common.collect.Sets;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.energy.NBTEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public abstract class TilePowered extends TileBase {
    private NBTEnergyHandler energyHandler;

    public TilePowered() {
        this.energyHandler = getEnergyHandlerFactory().create();
    }

    @Nonnull
    protected IFactory<NBTEnergyHandler> getEnergyHandlerFactory() {
        return () -> new NBTEnergyHandler(10000);
    }

    @Nonnull
    public IEnergyStorage getEnergyStorage() {
        return energyHandler;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        compound.setTag("energy", energyHandler.serializeNBT());
    }

    @Nonnull
    public Set<EnumFacing> getValidEnergyFaces() {
        return Sets.newHashSet(EnumFacing.VALUES);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (compound.hasKey("energy", Constants.NBT.TAG_INT))
            energyHandler.deserializeNBT((NBTTagInt) compound.getTag("energy"));
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? facing == null || getValidEnergyFaces().contains(facing) : super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY && getValidEnergyFaces().contains(facing) ? CapabilityEnergy.ENERGY.cast(energyHandler) : super.getCapability(capability, facing);
    }
}

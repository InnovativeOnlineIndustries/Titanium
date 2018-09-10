/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.block.tile;

import com.google.common.collect.Sets;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.energy.NBTEnergyHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Set;

public class TilePowered extends TileBase {

    @Save
    private NBTEnergyHandler energyHandler;

    public TilePowered() {
        this.energyHandler = getEnergyHandlerFactory().create();
    }

    protected IFactory<NBTEnergyHandler> getEnergyHandlerFactory() {
        return () -> new NBTEnergyHandler(this, 10000);
    }

    public IEnergyStorage getEnergyStorage() {
        return energyHandler;
    }

    public Set<EnumFacing> getValidEnergyFaces() {
        return Sets.newHashSet(EnumFacing.VALUES);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? facing == null || getValidEnergyFaces().contains(facing) : super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY && (facing == null || getValidEnergyFaces().contains(facing)) ? CapabilityEnergy.ENERGY.cast(energyHandler) : super.getCapability(capability, facing);
    }
}

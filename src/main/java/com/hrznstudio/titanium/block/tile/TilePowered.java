/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.google.common.collect.Sets;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.energy.NBTEnergyHandler;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public abstract class TilePowered<T extends TilePowered<T>> extends TileActive<T> {
    @Save
    private NBTEnergyHandler energyHandler;
    private LazyOptional<IEnergyStorage> energyCap;

    public TilePowered(BlockTileBase blockTileBase) {
        super(blockTileBase);
        this.energyHandler = getEnergyHandlerFactory().create();
        this.energyCap = LazyOptional.of(() -> this.energyHandler);
        //addGuiAddonFactory(() -> new EnergyBarGuiAddon(20, 20, energyHandler));
    }

    protected IFactory<NBTEnergyHandler> getEnergyHandlerFactory() {
        return () -> new NBTEnergyHandler(this, 10000);
    }

    public NBTEnergyHandler getEnergyStorage() {
        return energyHandler;
    }

    public Set<Direction> getValidEnergyFaces() {
        return Sets.newHashSet(Direction.values());
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY)
            return energyCap.cast();
        return super.getCapability(cap, side);
    }
}

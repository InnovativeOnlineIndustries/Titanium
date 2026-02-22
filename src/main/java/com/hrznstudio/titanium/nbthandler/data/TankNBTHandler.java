/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TankNBTHandler implements INBTHandler<FluidTank> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return FluidTank.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull FluidTank object) {
        compound.put(name, object.writeToNBT(provider, new CompoundTag()));
        return true;
    }

    @Override
    public FluidTank readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable FluidTank currentValue) {
        if (compound.contains(name)) {
            currentValue.readFromNBT(provider, compound.getCompound(name));
            return currentValue;
        }
        return currentValue;
    }
}

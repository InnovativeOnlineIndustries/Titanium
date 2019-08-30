/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TankNBTHandler implements INBTHandler<FluidTank> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return FluidTank.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull FluidTank object) {
        compound.put(name, object.writeToNBT(new CompoundNBT()));
        return true;
    }

    @Override
    public FluidTank readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable FluidTank currentValue) {
        if (compound.contains(name)) {
            currentValue.readFromNBT(compound.getCompound(name));
            return currentValue;
        }
        return currentValue;
    }
}

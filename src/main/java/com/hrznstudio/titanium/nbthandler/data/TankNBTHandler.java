/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public class TankNBTHandler implements INBTHandler<FluidTank> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return FluidTank.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull FluidTank object) {
        compound.setTag(name, object.writeToNBT(new NBTTagCompound()));
        return true;
    }

    @Override
    public FluidTank readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, FluidTank currentValue) {
        if (compound.hasKey(name)) {
            currentValue.readFromNBT(compound.getCompoundTag(name));
            return currentValue;
        }
        return null;
    }
}

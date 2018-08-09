/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.nbthandler.data;

import com.hrznstudio.titanium.base.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class DoubleNBTHandler implements INBTHandler<Double> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return float.class.isAssignableFrom(aClass) || Float.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull Double object) {
        compound.setDouble(name, object);
        return true;
    }

    @Override
    public Double readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, Double currentValue) {
        return compound.hasKey(name) ? compound.getDouble(name) : null;
    }
}

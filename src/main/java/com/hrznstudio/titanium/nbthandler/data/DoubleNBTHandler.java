/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class DoubleNBTHandler implements INBTHandler<Double> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return double.class.isAssignableFrom(aClass) || Double.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull Double object) {
        compound.putDouble(name, object);
        return true;
    }

    @Override
    public Double readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, Double currentValue) {
        return compound.contains(name) ? compound.getDouble(name) : null;
    }
}

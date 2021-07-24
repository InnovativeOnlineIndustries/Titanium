/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DoubleNBTHandler implements INBTHandler<Double> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return double.class.isAssignableFrom(aClass) || Double.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Double object) {
        compound.putDouble(name, object);
        return true;
    }

    @Override
    public Double readFromNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nullable Double currentValue) {
        return compound.contains(name) ? compound.getDouble(name) : Unboxing.safelyUnbox(currentValue);
    }
}

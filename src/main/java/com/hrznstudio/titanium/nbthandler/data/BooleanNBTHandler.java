/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BooleanNBTHandler implements INBTHandler<Boolean> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return boolean.class.isAssignableFrom(aClass) || Boolean.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Boolean object) {
        compound.putBoolean(name, object);
        return true;
    }

    @Override
    public Boolean readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Boolean currentValue) {
        return compound.contains(name) ? compound.getBoolean(name) : Unboxing.safelyUnbox(currentValue);
    }
}

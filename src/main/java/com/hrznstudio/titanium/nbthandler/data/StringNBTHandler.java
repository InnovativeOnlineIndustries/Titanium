/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringNBTHandler implements INBTHandler<String> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return String.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nonnull String object) {
        compound.putString(name, object);
        return true;
    }

    @Override
    public String readFromNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nullable String current) {
        return compound.contains(name) ? compound.getString(name) : current;
    }
}

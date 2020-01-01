/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NBTSerializableNBTHandler implements INBTHandler<INBTSerializable> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return INBTSerializable.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull INBTSerializable object) {
        compound.put(name, object.serializeNBT());
        return false;
    }

    @Override
    public INBTSerializable readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable INBTSerializable currentValue) {
        if (compound.contains(name) && currentValue != null) {
            currentValue.deserializeNBT(compound.get(name));
            return currentValue;
        }
        return currentValue;
    }
}

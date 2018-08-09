/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.nbthandler.data;

import com.hrznstudio.titanium.base.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class NBTSerializableNBTHandler implements INBTHandler<INBTSerializable> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return INBTSerializable.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull INBTSerializable object) {
        compound.setTag(name, object.serializeNBT());
        return false;
    }

    @Override
    public INBTSerializable readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, INBTSerializable currentValue) {
        if (compound.hasKey(name)) {
            currentValue.deserializeNBT(compound.getTag(name));
            return currentValue;
        }
        return null;
    }
}

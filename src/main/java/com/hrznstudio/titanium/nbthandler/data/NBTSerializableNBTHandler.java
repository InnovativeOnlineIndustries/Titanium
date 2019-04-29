/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.nbthandler.INBTHandler;
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
        compound.put(name, object.serializeNBT());
        return false;
    }

    @Override
    public INBTSerializable readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, INBTSerializable currentValue) {
        if (compound.contains(name)) {
            currentValue.deserializeNBT(compound.get(name));
            return currentValue;
        }
        return null;
    }
}

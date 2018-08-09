/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.nbthandler.data;

import com.hrznstudio.titanium.base.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class FloatNBTHandler implements INBTHandler<Float> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return float.class.isAssignableFrom(aClass) || Float.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull Float object) {
        compound.setFloat(name, object);
        return true;
    }

    @Override
    public Float readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, Float currentValue) {
        return compound.hasKey(name) ? compound.getFloat(name) : null;
    }
}

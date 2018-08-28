/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.nbthandler.data;

import com.hrznstudio.titanium.base.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class StringNBTHandler implements INBTHandler<String> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return String.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull String object) {
        compound.setString(name, object);
        return true;
    }

    @Override
    public String readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, String current) {
        return compound.hasKey(name, Constants.NBT.TAG_STRING) ? compound.getString(name) : null;
    }
}

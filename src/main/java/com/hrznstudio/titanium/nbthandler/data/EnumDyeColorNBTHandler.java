/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.nbthandler.INBTHandler;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class EnumDyeColorNBTHandler implements INBTHandler<EnumDyeColor> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return EnumDyeColor.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull EnumDyeColor object) {
        compound.putInt(name, object.getId());
        return true;
    }

    @Override
    public EnumDyeColor readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, EnumDyeColor current) {
        return compound.contains(name) ? EnumDyeColor.byId(compound.getInt(name)) : current;
    }
}
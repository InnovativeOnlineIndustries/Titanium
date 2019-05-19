/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
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
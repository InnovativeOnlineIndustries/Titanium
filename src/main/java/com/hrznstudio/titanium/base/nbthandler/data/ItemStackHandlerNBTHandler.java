/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.nbthandler.data;


import com.hrznstudio.titanium.base.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemStackHandlerNBTHandler implements INBTHandler<ItemStackHandler> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return ItemStackHandler.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull ItemStackHandler object) {
        compound.setTag(name, object.serializeNBT());
        return true;
    }

    @Override
    public ItemStackHandler readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, ItemStackHandler current) {
        if (compound.hasKey(name)) {
            if (current == null) current = new ItemStackHandler();
            current.deserializeNBT(compound.getCompoundTag(name));
            return current;
        }
        return null;
    }
}

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
        if (compound.hasKey(name, Constants.NBT.TAG_COMPOUND)) {
            if (current == null) current = new ItemStackHandler();
            current.deserializeNBT(compound.getCompoundTag(name));
            return current;
        }
        return null;
    }
}

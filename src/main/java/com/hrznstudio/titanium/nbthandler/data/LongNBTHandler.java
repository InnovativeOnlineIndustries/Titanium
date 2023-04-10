/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LongNBTHandler implements INBTHandler<Long> {

    /**
     * Checks if the NBTHanlder can handle a class.
     *
     * @param aClass The class that wants to be checked.
     * @return true if the capability can handle the class or false if it can't.
     */
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return long.class.isAssignableFrom(aClass) || Long.class.isAssignableFrom(aClass);
    }

    /**
     * Stores a value as the given name in the NBT.
     *
     * @param compound The NBT where the object needs to be stored.
     * @param name     The name as it will be stored.
     * @param object   The object value to be stored.
     * @return true if the Object was successfully stored in the NBT
     */
    @Override
    public boolean storeToNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Long object) {
        compound.putLong(name, object);
        return true;
    }

    /**
     * Reads the value from the NBT to be stored in the Field.
     *
     * @param compound The NBT that stores all the information.
     * @param name     The name of the object stored in the NBT.
     * @return The object if it was successfully stored or null if it wasn't giving the next handlers a chance to store the value.
     */
    @Override
    public Long readFromNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nullable Long current) {
        return compound.contains(name) ? compound.getLong(name) : Unboxing.safelyUnbox(current);
    }
}

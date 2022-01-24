/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface INBTHandler<T> {

    /**
     * Checks if the NBTHandler can handle a class.
     *
     * @param aClass The class that wants to be checked.
     * @return true if the capability can handle the class or false if it can't.
     */
    boolean isClassValid(Class<?> aClass);

    /**
     * Stores a value as the given name in the NBT.
     *
     * @param compound The NBT where the object needs to be stored.
     * @param name     The name as it will be stored.
     * @param object   The object value to be stored.
     * @return true if the Object was successfully stored in the NBT
     */
    boolean storeToNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nonnull T object);

    /**
     * Reads the value from the NBT to be stored in the Field.
     *
     * @param compound     The NBT that stores all the information.
     * @param name         The name of the object stored in the NBT.
     * @param currentValue The current value of the object
     * @return The object if it was successfully stored or null if it wasn't giving the next handlers a chance to store the value.
     */
    T readFromNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nullable T currentValue);
}

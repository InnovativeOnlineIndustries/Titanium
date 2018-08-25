/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.nbthandler;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface INBTHandler<T> {

    /**
     * Checks if the NBTHanlder can handle a class.
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
    boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull T object);

    /**
     * Reads the value from the NBT to be stored in the Field.
     *
     * @param compound     The NBT that stores all the information.
     * @param name         The name of the object stored in the NBT.
     * @param currentValue The current value of the object
     * @return The object if it was successfully stored or null if it wasn't giving the next handlers a chance to store the value.
     */
    T readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, T currentValue);
}

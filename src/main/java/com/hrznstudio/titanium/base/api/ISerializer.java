/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.api;

import net.minecraft.nbt.NBTTagCompound;

public interface ISerializer<T> {
    boolean canHandle(Class<?> type);

    void serialize(String name, T object, NBTTagCompound nbt);

    T deserialize(String name, NBTTagCompound nbt);
}
/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.api.component;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IDataComponent extends IComponent {
    void store(String name, int value);
    void store(String name, double value);
    void store(String name, float value);
    void store(String name, String value);
    void store(String name, NBTTagCompound value);
    void store(String name, NBTBase value);
    void store(String name, boolean value);

    void read(String name, int value);
    void read(String name, double value);
    void read(String name, float value);
    void read(String name, String value);
    void read(String name, NBTTagCompound value);
    void read(String name, NBTBase value);
    void read(String name, boolean value);
}

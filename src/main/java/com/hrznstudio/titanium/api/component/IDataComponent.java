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

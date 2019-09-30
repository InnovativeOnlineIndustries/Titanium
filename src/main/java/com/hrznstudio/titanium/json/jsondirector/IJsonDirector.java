package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.util.ResourceLocation;

public interface IJsonDirector<T> {
    void put(ResourceLocation resourceLocation, T value);

    void clear();
}

package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class MapJsonDirector<T> implements IJsonDirector<T> {
    private final Map<ResourceLocation, T> map;

    public MapJsonDirector(Map<ResourceLocation, T> map) {
        this.map = map;
    }

    @Override
    public void put(ResourceLocation resourceLocation, T value) {
        map.put(resourceLocation, value);
    }

    @Override
    public void clear() {
        map.clear();
    }
}

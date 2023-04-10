/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.resources.ResourceLocation;

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

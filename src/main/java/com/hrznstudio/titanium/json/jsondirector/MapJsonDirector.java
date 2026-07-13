/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.resources.Identifier;

import java.util.Map;

public class MapJsonDirector<T> implements IJsonDirector<T> {
    private final Map<Identifier, T> map;

    public MapJsonDirector(Map<Identifier, T> map) {
        this.map = map;
    }

    @Override
    public void put(Identifier resourceLocation, T value) {
        map.put(resourceLocation, value);
    }

    @Override
    public void clear() {
        map.clear();
    }
}

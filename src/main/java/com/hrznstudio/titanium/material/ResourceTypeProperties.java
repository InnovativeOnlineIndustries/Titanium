/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;

public class ResourceTypeProperties<T> {

    public static HashMap<Class<? extends ForgeRegistryEntry>, ResourceTypeProperties> DEFAULTS = new HashMap<>();

    public final T properties;

    public ResourceTypeProperties(T properties) {
        this.properties = properties;
    }

    public T get() {
        return properties;
    }
}

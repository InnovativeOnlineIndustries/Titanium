/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.util.ResourceLocation;

public interface IJsonDirector<T> {
    void put(ResourceLocation resourceLocation, T value);

    void clear();
}

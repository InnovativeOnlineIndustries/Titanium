/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.proton.api;

import net.minecraftforge.registries.IForgeRegistryEntry;

public interface RegistryManager {
    <T extends IForgeRegistryEntry<T>> void addEntry(Class<T> tClass, T t);

    <T extends IForgeRegistryEntry<T>> void addEntries(Class<T> tClass, T... ts);
}

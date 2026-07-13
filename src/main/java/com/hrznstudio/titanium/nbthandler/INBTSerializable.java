/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;

/**
 * Titanium's stable NBT serialization contract.
 *
 * <p>NeoForge removed its legacy {@code INBTSerializable} interface in 26.1.
 * Keeping the contract inside Titanium preserves the library API and existing
 * serialization behavior while Minecraft transitions its own persistence APIs.</p>
 */
public interface INBTSerializable<T extends Tag> {

    T serializeNBT(HolderLookup.Provider provider);

    void deserializeNBT(HolderLookup.Provider provider, T nbt);
}

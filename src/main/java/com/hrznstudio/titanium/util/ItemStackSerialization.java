/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;

public final class ItemStackSerialization {
    private ItemStackSerialization() {
    }

    public static CompoundTag save(HolderLookup.Provider provider, ItemStack stack) {
        return (CompoundTag) ItemStack.OPTIONAL_CODEC
            .encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), stack)
            .result()
            .orElseGet(CompoundTag::new);
    }

    public static ItemStack load(HolderLookup.Provider provider, CompoundTag tag) {
        return ItemStack.OPTIONAL_CODEC
            .parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
            .result()
            .orElse(ItemStack.EMPTY);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public final class ValueIOSerialization {
    private ValueIOSerialization() {
    }

    public static CompoundTag save(HolderLookup.Provider provider, ValueIOSerializable serializable) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, provider);
        serializable.serialize(output);
        return output.buildResult();
    }

    public static void load(HolderLookup.Provider provider, CompoundTag tag, ValueIOSerializable serializable) {
        serializable.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, provider, tag));
    }
}

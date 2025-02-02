/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.attachment;

import com.hrznstudio.titanium.item.EnergyItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;

public record StoredEnergyAttachment(int stored, int capacity, int in, int out) {
    public StoredEnergyAttachment(EnergyItem energyItem) {
        this(0, energyItem.getCapacity(), energyItem.getInput(), energyItem.getOutput());
    }

    public static final Codec<StoredEnergyAttachment> CODEC = RecordCodecBuilder.create(in -> in.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("stored").forGetter(StoredEnergyAttachment::stored),
        ExtraCodecs.POSITIVE_INT.fieldOf("capacity").forGetter(StoredEnergyAttachment::capacity),
        ExtraCodecs.POSITIVE_INT.fieldOf("in").forGetter(StoredEnergyAttachment::in),
        ExtraCodecs.POSITIVE_INT.fieldOf("out").forGetter(StoredEnergyAttachment::out)
    ).apply(in, StoredEnergyAttachment::new));
    public static DeferredHolder<DataComponentType<?>, DataComponentType<StoredEnergyAttachment>> TYPE;
}

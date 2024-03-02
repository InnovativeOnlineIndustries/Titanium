/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class EnabledRewards implements INBTSerializable<CompoundTag> {

    private HashMap<ResourceLocation, String> enabled;

    public EnabledRewards() {
        this.enabled = new HashMap<>();
    }

    public HashMap<ResourceLocation, String> getEnabled() {
        return enabled;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        enabled.forEach((resourceLocation, s) -> compoundNBT.putString(resourceLocation.toString(), s));
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        enabled.clear();
        nbt.getAllKeys().forEach(s -> enabled.put(new ResourceLocation(s), nbt.getString(s)));
    }
}

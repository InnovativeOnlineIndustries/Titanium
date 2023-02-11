/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.UUID;

public class ClientRewardStorage implements INBTSerializable<CompoundTag> {

    public static final ClientRewardStorage REWARD_STORAGE = new ClientRewardStorage();

    private HashMap<UUID, EnabledRewards> rewards;

    public ClientRewardStorage() {
        this.rewards = new HashMap<>();
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        rewards.clear();
        nbt.getAllKeys().forEach(s -> {
            EnabledRewards rewards = new EnabledRewards();
            rewards.deserializeNBT(nbt.getCompound(s));
            this.rewards.put(UUID.fromString(s), rewards);
        });
    }

    public HashMap<UUID, EnabledRewards> getRewards() {
        return rewards;
    }
}

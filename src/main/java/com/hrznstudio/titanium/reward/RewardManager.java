/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RewardManager {

    private static RewardManager INSTANCE = new RewardManager();
    private HashMap<UUID, RewardGiver> rewards;

    private RewardManager() {
        this.rewards = new HashMap<>();
    }

    public static RewardManager get() {
        return INSTANCE;
    }

    public RewardGiver getGiver(UUID uuid, String name) {
        return rewards.computeIfAbsent(uuid, uuid1 -> new RewardGiver(uuid, name));
    }

    public HashMap<UUID, RewardGiver> getRewards() {
        return rewards;
    }

    public List<ResourceLocation> collectRewardsResourceLocations(UUID uuid) {
        return rewards.values().stream().map(rewardGiver -> rewardGiver.collectRewardsResourceLocations(uuid)).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Reward getReward(ResourceLocation resourceLocation) {
        for (RewardGiver value : rewards.values()) {
            for (Reward reward : value.getRewards()) {
                if (reward.getResourceLocation().equals(resourceLocation)) return reward;
            }
        }
        return null;
    }
}

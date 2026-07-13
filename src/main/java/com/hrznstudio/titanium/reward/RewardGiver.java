/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RewardGiver {

    private final UUID uuid;
    private final String name;
    private List<Reward> rewards;

    public RewardGiver(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.rewards = new ArrayList<>();
    }

    public void addReward(Reward reward) {
        this.rewards.add(reward);
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public List<Identifier> collectRewardsIdentifiers(UUID uuid) {
        return rewards.stream().filter(reward -> reward.isPlayerValid(uuid)).map(Reward::getIdentifier).collect(Collectors.toList());
    }
}

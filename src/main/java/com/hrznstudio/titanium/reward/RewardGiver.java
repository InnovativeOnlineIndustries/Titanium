package com.hrznstudio.titanium.reward;

import net.minecraft.util.ResourceLocation;

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

    public List<ResourceLocation> collectRewardsResourceLocations(UUID uuid) {
        return rewards.stream().filter(reward -> reward.isPlayerValid(uuid)).map(Reward::getResourceLocation).collect(Collectors.toList());
    }
}

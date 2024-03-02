/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RewardWorldStorage extends SavedData {

    public static String NAME = "TitaniumReward";
    private HashMap<UUID, EnabledRewards> rewards;
    private List<ResourceLocation> freeRewards;
    private List<UUID> configuredPlayers;

    private RewardWorldStorage() {
        this.rewards = new HashMap<>();
        this.freeRewards = new ArrayList<>();
        this.configuredPlayers = new ArrayList<>();
    }

    public static RewardWorldStorage get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(compoundTag -> new RewardWorldStorage().load(compoundTag), RewardWorldStorage::new, NAME);
    }

    public void remove(UUID uuid, ResourceLocation resourceLocation) {
        rewards.computeIfAbsent(uuid, uuid1 -> new EnabledRewards()).getEnabled().remove(resourceLocation);
    }

    public void add(UUID uuid, ResourceLocation resourceLocation, String option) {
        rewards.computeIfAbsent(uuid, uuid1 -> new EnabledRewards()).getEnabled().put(resourceLocation, option);
    }

    public void addFree(ResourceLocation resourceLocation) {
        freeRewards.add(resourceLocation);
    }

    public List<ResourceLocation> getFreeRewards() {
        return freeRewards;
    }

    public List<UUID> getConfiguredPlayers() {
        return configuredPlayers;
    }


    public RewardWorldStorage load(CompoundTag nbt) {
        CompoundTag compoundNBT = nbt.getCompound(NAME);
        rewards.clear();
        compoundNBT.getAllKeys().forEach(s -> {
            EnabledRewards rewards = new EnabledRewards();
            rewards.deserializeNBT(compoundNBT.getCompound(s));
            this.rewards.put(UUID.fromString(s), rewards);
        });
        freeRewards.clear();
        CompoundTag free = nbt.getCompound("FreeRewards");
        free.getAllKeys().forEach(s -> freeRewards.add(new ResourceLocation(s)));
        configuredPlayers.clear();
        CompoundTag configured = nbt.getCompound("ConfiguredPlayers");
        configured.getAllKeys().forEach(s -> configuredPlayers.add(UUID.fromString(s)));
        return this;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        CompoundTag compoundNBT = new CompoundTag();
        rewards.forEach((uuid, enabledRewards) -> compoundNBT.put(uuid.toString(), enabledRewards.serializeNBT()));
        compound.put(NAME, compoundNBT);
        CompoundTag free = new CompoundTag();
        freeRewards.forEach(resourceLocation -> free.putBoolean(resourceLocation.toString(), true));
        compound.put("FreeRewards", free);
        CompoundTag configured = new CompoundTag();
        configuredPlayers.forEach(uuid -> configured.putBoolean(uuid.toString(), true));
        compound.put("ConfiguredPlayers", configured);
        return compound;
    }

    public CompoundTag serializeSimple() {
        CompoundTag compoundNBT = new CompoundTag();
        rewards.forEach((uuid, enabledRewards) -> compoundNBT.put(uuid.toString(), enabledRewards.serializeNBT()));
        return compoundNBT;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RewardWorldStorage extends WorldSavedData {

    public static String NAME = "TitaniumReward";
    private HashMap<UUID, EnabledRewards> rewards;
    private List<ResourceLocation> freeRewards;
    private List<UUID> configuredPlayers;

    private RewardWorldStorage(String p_i2141_1_) {
        super(NAME);
        this.rewards = new HashMap<>();
        this.freeRewards = new ArrayList<>();
        this.configuredPlayers = new ArrayList<>();
    }

    public RewardWorldStorage() {
        this(NAME);
    }

    public static RewardWorldStorage get(ServerWorld world) {
        return world.getSavedData().getOrCreate(RewardWorldStorage::new, NAME);
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

    @Override
    public void read(CompoundNBT nbt) {
        CompoundNBT compoundNBT = nbt.getCompound(NAME);
        rewards.clear();
        compoundNBT.keySet().forEach(s -> {
            EnabledRewards rewards = new EnabledRewards();
            rewards.deserializeNBT(compoundNBT.getCompound(s));
            this.rewards.put(UUID.fromString(s), rewards);
        });
        freeRewards.clear();
        CompoundNBT free = nbt.getCompound("FreeRewards");
        free.keySet().forEach(s -> freeRewards.add(new ResourceLocation(s)));
        configuredPlayers.clear();
        CompoundNBT configured = nbt.getCompound("ConfiguredPlayers");
        configured.keySet().forEach(s -> configuredPlayers.add(UUID.fromString(s)));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT compoundNBT = new CompoundNBT();
        rewards.forEach((uuid, enabledRewards) -> compoundNBT.put(uuid.toString(), enabledRewards.serializeNBT()));
        compound.put(NAME, compoundNBT);
        CompoundNBT free = new CompoundNBT();
        freeRewards.forEach(resourceLocation -> free.putBoolean(resourceLocation.toString(), true));
        compound.put("FreeRewards", free);
        CompoundNBT configured = new CompoundNBT();
        configuredPlayers.forEach(uuid -> configured.putBoolean(uuid.toString(), true));
        compound.put("ConfiguredPlayers", configured);
        return compound;
    }

    public CompoundNBT serializeSimple() {
        CompoundNBT compoundNBT = new CompoundNBT();
        rewards.forEach((uuid, enabledRewards) -> compoundNBT.put(uuid.toString(), enabledRewards.serializeNBT()));
        return compoundNBT;
    }
}

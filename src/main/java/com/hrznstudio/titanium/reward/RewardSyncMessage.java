/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward;

import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.reward.storage.ClientRewardStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.NetworkEvent;


public class RewardSyncMessage extends Message {

    private CompoundTag compoundNBT;

    public RewardSyncMessage(CompoundTag compoundNBT) {
        this.compoundNBT = compoundNBT;
    }

    public RewardSyncMessage() {

    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        Minecraft.getInstance().tell(() -> {
            ClientRewardStorage.REWARD_STORAGE.deserializeNBT(compoundNBT);
        });
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IButtonHandler {
    void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound);
}

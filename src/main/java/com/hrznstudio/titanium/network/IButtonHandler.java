/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface IButtonHandler {
    void handleButtonMessage(int id, Player playerEntity, CompoundTag compound);
}

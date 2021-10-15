/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity<T extends BlockEntity> {

    default void serverTick(Level level, BlockPos pos, BlockState state, T blockEntity){

    }

    default void clientTick(Level level, BlockPos pos, BlockState state, T blockEntity){

    }

}

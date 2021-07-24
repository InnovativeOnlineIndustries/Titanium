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

/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;

public interface IMultiblock extends IForgeRegistryEntry<Block> {

    Predicate<BlockState>[][][] getStructureBlocks();

    boolean isController(int x, int y, int z);

    boolean isStructureMultiblock(Level world, BlockPos pos, BlockState state, Direction playerFacing);

    void createStructureMultiblock(Level world, BlockPos pos, BlockState state, Direction playerFacing);

    void destroyMultiblock(Level world, BlockPos pos, BlockState state, Direction facing);

}

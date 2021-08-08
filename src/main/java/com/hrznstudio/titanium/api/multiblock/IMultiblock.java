/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;

public interface IMultiblock extends IForgeRegistryEntry<Block> {

    Predicate<BlockState>[][][] getStructureBlocks();

    boolean isController(int x, int y, int z);

    boolean isStructureMultiblock(World world, BlockPos pos, BlockState state, Direction playerFacing);

    void createStructureMultiblock(World world, BlockPos pos, BlockState state, Direction playerFacing);

    void destroyMultiblock(World world, BlockPos pos, BlockState state, Direction facing);

}

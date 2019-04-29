/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.multiblock;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;

public interface IMultiblock extends IForgeRegistryEntry<Block> {

    Predicate<IBlockState>[][][] getStructureBlocks();

    boolean isController(int x, int y, int z);

    boolean isStructureMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing playerFacing);

    void createStructureMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing playerFacing);

    void destroyMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing facing);

}

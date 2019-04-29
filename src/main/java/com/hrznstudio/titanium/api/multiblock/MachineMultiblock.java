/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.api.multiblock;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MachineMultiblock implements IMultiblock {

    private final ResourceLocation resourceLocation;
    private final Predicate<IBlockState>[][][] multiblockStructure;
    private final BlockPos controller;

    public MachineMultiblock(ResourceLocation resourceLocation, Predicate<IBlockState>[][][] multiblockStructure, BlockPos controller) {
        this.resourceLocation = resourceLocation;
        this.multiblockStructure = multiblockStructure;
        this.controller = controller;
    }

    @Override
    public Predicate<IBlockState>[][][] getStructureBlocks() {
        return multiblockStructure;
    }

    @Override
    public boolean isController(int x, int y, int z) {
        return controller.equals(new BlockPos(x, y, z));
    }

    @Override
    public boolean isStructureMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing playerFacing) {
        for (int x = 0; x < getStructureBlocks().length; ++x) {
            for (int z = 0; z < getStructureBlocks()[0][0].length; ++z) {
                for (int y = 0; y < getStructureBlocks()[0].length; ++y) {
                    BlockPos blockPos = pos.offset(EnumFacing.DOWN, controller.getY()).offset(playerFacing.rotateY(), x).offset(EnumFacing.UP, y).offset(playerFacing, z);
                    IBlockState test = world.getBlockState(blockPos);
                    if (!getStructureBlocks()[x][y][z].test(test)) { //TODO Check for controller blocks
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void createStructureMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing playerFacing) {

    }

    @Override
    public void destroyMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing facing) {

    }

    @Override
    public Block setRegistryName(ResourceLocation name) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }

    @Override
    public Class<Block> getRegistryType() {
        return null;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MachineMultiblock implements IMultiblock {

    private final ResourceLocation resourceLocation;
    private final Predicate<BlockState>[][][] multiblockStructure;
    private final BlockPos controller;

    public MachineMultiblock(ResourceLocation resourceLocation, Predicate<BlockState>[][][] multiblockStructure, BlockPos controller) {
        this.resourceLocation = resourceLocation;
        this.multiblockStructure = multiblockStructure;
        this.controller = controller;
    }

    @Override
    public Predicate<BlockState>[][][] getStructureBlocks() {
        return multiblockStructure;
    }

    @Override
    public boolean isController(int x, int y, int z) {
        return controller.equals(new BlockPos(x, y, z));
    }

    @Override
    public boolean isStructureMultiblock(World world, BlockPos pos, BlockState state, Direction playerFacing) {
        for (int x = 0; x < getStructureBlocks().length; ++x) {
            for (int z = 0; z < getStructureBlocks()[0][0].length; ++z) {
                for (int y = 0; y < getStructureBlocks()[0].length; ++y) {
                    BlockPos blockPos = pos.offset(Direction.DOWN, controller.getY()).offset(playerFacing.rotateY(), x).offset(Direction.UP, y).offset(playerFacing, z);
                    BlockState test = world.getBlockState(blockPos);
                    if (!getStructureBlocks()[x][y][z].test(test)) { //TODO Check for controller blocks
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void createStructureMultiblock(World world, BlockPos pos, BlockState state, Direction playerFacing) {

    }

    @Override
    public void destroyMultiblock(World world, BlockPos pos, BlockState state, Direction facing) {

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

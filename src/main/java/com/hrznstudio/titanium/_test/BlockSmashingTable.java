/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium._test;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.List;

public class BlockSmashingTable extends BlockBase {
    public static float PIXEL = 1f / 16f;

    protected static final VoxelShape BOTTOM_AABB = VoxelShapes.create(new AxisAlignedBB(PIXEL, 0.0D, PIXEL, PIXEL * 15, PIXEL * 8, PIXEL * 15));
    protected static final VoxelShape TOP_AABB = VoxelShapes.create(new AxisAlignedBB(0.0D, PIXEL * 8, 0.0D, 1.0D, PIXEL * 11, 1.0D));
    protected static final VoxelShape BLOCK_AABB = VoxelShapes.create(new AxisAlignedBB(PIXEL * 4, PIXEL * 11, PIXEL * 4, PIXEL * 12, PIXEL * 18.5, PIXEL * 12));
    protected static final VoxelShape TOP = VoxelShapes.combineAndSimplify(TOP_AABB, BLOCK_AABB, IBooleanFunction.OR);

    public BlockSmashingTable() {
        super("smashing_table", Properties.create(Material.IRON));
    }

    @Override
    public List<VoxelShape> getBoundingBoxes(IBlockState state, IBlockReader source, BlockPos pos) {
        return Lists.newArrayList(
                BOTTOM_AABB,
                TOP_AABB,
                BLOCK_AABB
        );
    }

    @Override
    public boolean hasCustomBoxes(IBlockState state, IBlockReader source, BlockPos pos) {
        return true;
    }
}
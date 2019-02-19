/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.internal.IItemBlockFactory;
import com.hrznstudio.titanium.api.internal.IModelRegistrar;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class BlockBase extends Block implements IModelRegistrar, IItemBlockFactory {

    public BlockBase(String name, Properties properties) {
        super(properties);
        setRegistryName(name);
    }

    @Nullable
    protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3d start, Vec3d end, VoxelShape shape) {
        RayTraceResult bbResult = shape.func_212433_a(start, end, pos);
        if (bbResult != null) {
            Vec3d hitVec = bbResult.hitVec;
            EnumFacing sideHit = bbResult.sideHit;
            double dist = start.distanceTo(hitVec);
            return new DistanceRayTraceResult(hitVec, sideHit, pos, shape, dist);
        }
        return null;
    }

    @Nullable
    @Override
    public RayTraceResult getRayTraceResult(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end, RayTraceResult original) {
        if (hasCustomBoxes(state, world, pos)) {
            return rayTraceBoxesClosest(start, end, pos, getBoundingBoxes(state, world, pos));
        }
        return super.getRayTraceResult(state, world, pos, start, end, original);
    }

    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        if (hasCustomBoxes(state, worldIn, pos)) {
            VoxelShape shape = null;
            for (VoxelShape shape1 : getBoundingBoxes(state, worldIn, pos)) {
                if (shape == null) {
                    shape = shape1;
                } else {
                    shape = VoxelShapes.combineAndSimplify(shape, shape1, IBooleanFunction.OR);
                }
            }
            return shape;
        }
        return super.getCollisionShape(state, worldIn, pos);
    }

    @Override
    public IFactory<ItemBlock> getItemBlockFactory() {
        return () -> (ItemBlock) new ItemBlock(this, new Item.Properties()).setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

    @Override
    public void registerModels() {
        //ClientUtil.registerToMapper(this);
    }

    public List<VoxelShape> getBoundingBoxes(IBlockState state, IBlockReader source, BlockPos pos) {
        return Collections.emptyList();
    }

    public boolean hasCustomBoxes(IBlockState state, IBlockReader source, BlockPos pos) {
        return false;
    }


    @Nullable
    protected RayTraceResult rayTraceBoxesClosest(Vec3d start, Vec3d end, BlockPos pos, List<VoxelShape> boxes) {
        List<DistanceRayTraceResult> results = new ArrayList<>();
        for (VoxelShape box : boxes) {
            DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
            if (hit != null)
                results.add(hit);
        }
        RayTraceResult closestHit = null;
        double curClosest = Double.MAX_VALUE;
        for (DistanceRayTraceResult hit : results) {
            if (curClosest > hit.getDistance()) {
                closestHit = hit;
                curClosest = hit.getDistance();
            }
        }
        return closestHit;
    }
}
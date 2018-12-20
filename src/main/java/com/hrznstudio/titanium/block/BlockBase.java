/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.internal.IItemBlockFactory;
import com.hrznstudio.titanium.api.internal.IModelRegistrar;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.api.raytrace.IndexedAxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class BlockBase extends Block implements IModelRegistrar, IItemBlockFactory {

    public BlockBase(String name, Builder properties) {
        super(properties);
        setRegistryName(name);
    }

    @Nullable
    protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3d start, Vec3d end, IndexedAxisAlignedBB box) {
        Vec3d startRay = start.subtract(new Vec3d(pos));
        Vec3d endRay = end.subtract(new Vec3d(pos));
        RayTraceResult bbResult = box.calculateIntercept(startRay, endRay);

        if (bbResult != null) {
            Vec3d hitVec = bbResult.hitVec.add(new Vec3d(pos));
            EnumFacing sideHit = bbResult.sideHit;
            double dist = start.squareDistanceTo(hitVec);
            return new DistanceRayTraceResult(hitVec, sideHit, pos, box, dist);
        }
        return null;
    }

    @Override
    public IFactory<ItemBlock> getItemBlockFactory() {
        return () -> (ItemBlock) new ItemBlock(this, new Item.Builder()).setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

    @Override
    public void registerModels() {
        //ClientUtil.registerToMapper(this);
    }

    public List<IndexedAxisAlignedBB> getBoundingBoxes(IBlockState state, IWorldReader source, BlockPos pos) {
        return Collections.emptyList();
    }

    public boolean hasCustomBoxes(IBlockState state, IWorldReader source, BlockPos pos) {
        return false; //TODO: Reimplement custom boxes
    }


    @Nullable
    protected RayTraceResult rayTraceBoxesClosest(Vec3d start, Vec3d end, BlockPos pos, List<IndexedAxisAlignedBB> boxes) {
        List<DistanceRayTraceResult> results = new ArrayList<>();
        for (IndexedAxisAlignedBB box : boxes) {
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
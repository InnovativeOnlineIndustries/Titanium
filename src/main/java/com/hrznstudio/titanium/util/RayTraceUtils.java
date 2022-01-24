/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.block.BasicBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class RayTraceUtils {

    public static HitResult rayTraceSimple(Level world, LivingEntity living, double blockReachDistance, float partialTicks) {
        Vec3 vec3d = living.getEyePosition(partialTicks);
        Vec3 vec3d1 = living.getViewVector(partialTicks);
        Vec3 vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return world.clip(new ClipContext(vec3d, vec3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, living));
    }

    @Nullable
    public static VoxelShape rayTraceVoxelShape(BlockHitResult original, Level world, LivingEntity living, double blockReachDistance, float partialTicks) {
        BlockState og = world.getBlockState(original.getBlockPos());
        if (og.getBlock() instanceof BasicBlock && ((BasicBlock) og.getBlock()).hasIndividualRenderVoxelShape()) {
            Vec3 vec3d = living.getEyePosition(partialTicks);
            Vec3 vec3d1 = living.getViewVector(partialTicks);
            Vec3 vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
            List<VoxelShape> voxelShapes = ((BasicBlock) og.getBlock()).getBoundingBoxes(og, world, ((BlockHitResult) original).getBlockPos());
            if (voxelShapes.size() > 0){
                VoxelShape closest = Shapes.empty();
                double distance = Double.MAX_VALUE;
                for (VoxelShape voxelShape : voxelShapes) {
                    BlockHitResult result = voxelShape.clip(vec3d, vec3d2, ((BlockHitResult) original).getBlockPos());
                    if (result != null && vec3d.distanceTo(result.getLocation()) < distance) {
                        closest = voxelShape;
                        distance = vec3d.distanceTo(result.getLocation());
                    }
                }
                return closest;
            }
        }
        return Shapes.empty();
    }
}

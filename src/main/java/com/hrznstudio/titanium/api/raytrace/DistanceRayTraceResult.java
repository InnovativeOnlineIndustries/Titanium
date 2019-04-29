/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;

public class DistanceRayTraceResult extends RayTraceResult {
    private double distance;

    public DistanceRayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn, VoxelShape box, double distance) {
        super(hitVecIn, sideHitIn, blockPosIn);
        this.hitInfo = box;
        this.distance = distance;
    }

    public VoxelShape getHitBox() {
        return (VoxelShape) hitInfo;
    }

    public double getDistance() {
        return distance;
    }
}
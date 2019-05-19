/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
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
/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class DistanceRayTraceResult extends RayTraceResult {
    public DistanceRayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn, IndexedAxisAlignedBB box, double distance) {
        super(hitVecIn, sideHitIn, blockPosIn);
        this.hitInfo = box;
        this.distance = distance;
    }

    private double distance;

    public IndexedAxisAlignedBB getHitBox() {
        return (IndexedAxisAlignedBB) hitInfo;
    }

    public double getDistance() {
        return distance;
    }
}
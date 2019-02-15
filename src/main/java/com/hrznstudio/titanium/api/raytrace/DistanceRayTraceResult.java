/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class DistanceRayTraceResult extends RayTraceResult {
    private double distance;

    public DistanceRayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn, IndexedAxisAlignedBB box, double distance) {
        super(hitVecIn, sideHitIn, blockPosIn);
        this.hitInfo = box;
        this.distance = distance;
    }

    public IndexedAxisAlignedBB getHitBox() {
        return (IndexedAxisAlignedBB) hitInfo;
    }

    public double getDistance() {
        return distance;
    }
}
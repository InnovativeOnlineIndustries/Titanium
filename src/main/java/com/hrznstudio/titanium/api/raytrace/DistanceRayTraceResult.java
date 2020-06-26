/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;

public class DistanceRayTraceResult extends RayTraceResult {
    private double distance;

    public DistanceRayTraceResult(Vector3d hitVecIn, Direction sideHitIn, BlockPos blockPosIn, VoxelShape box, double distance) {
        super(hitVecIn);
        this.hitInfo = box;
        this.distance = distance;
    }

    public VoxelShape getHitBox() {
        return (VoxelShape) hitInfo;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public Type getType() {
        return Type.BLOCK;
    }
}

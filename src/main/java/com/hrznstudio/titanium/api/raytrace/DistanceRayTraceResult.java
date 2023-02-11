/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DistanceRayTraceResult extends HitResult {
    private double distance;
    private VoxelShape hitInfo;

    public DistanceRayTraceResult(Vec3 hitVecIn, Direction sideHitIn, BlockPos blockPosIn, VoxelShape box, double distance) {
        super(hitVecIn);
        this.hitInfo = box;
        this.distance = distance;
    }

    public VoxelShape getHitBox() {
        return hitInfo;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public Type getType() {
        return Type.BLOCK;
    }
}

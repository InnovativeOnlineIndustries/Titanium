/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class IndexedAxisAlignedBB extends AxisAlignedBB {
    private int index = 0;
    private boolean collidable = false;

    public IndexedAxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    public IndexedAxisAlignedBB(BlockPos pos) {
        super(pos);
    }

    public IndexedAxisAlignedBB(BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
    }

    public IndexedAxisAlignedBB(Vec3d min, Vec3d max) {
        super(min, max);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable() {
        this.collidable = true;
    }
}
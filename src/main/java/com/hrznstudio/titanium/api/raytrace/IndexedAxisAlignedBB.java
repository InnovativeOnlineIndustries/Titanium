/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.raytrace;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

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

    public IndexedAxisAlignedBB(Vector3d min, Vector3d max) {
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

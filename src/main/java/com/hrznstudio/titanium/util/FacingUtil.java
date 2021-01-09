/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class FacingUtil {

    public static Sideness getFacingRelative(Direction relative, @Nullable Direction facing) {
        if (facing == null) return null;
        if (facing == Direction.UP) return Sideness.TOP;
        if (facing == Direction.DOWN) return Sideness.BOTTOM;
        if (relative == facing) return Sideness.FRONT;
        if (relative == facing.getOpposite()) return Sideness.BACK;
        if (relative == facing.rotateYCCW()) return Sideness.LEFT;
        if (relative == facing.rotateY()) return Sideness.RIGHT;
        return Sideness.BOTTOM;
    }

    public static Direction getFacingFromSide(Direction block, Sideness sideness) {
        if (block.getAxis().isHorizontal()) {
            if (sideness == Sideness.TOP) return Direction.UP;
            if (sideness == Sideness.BOTTOM) return Direction.DOWN;
            if (sideness == Sideness.FRONT) return block;
            if (sideness == Sideness.BACK) return block.getOpposite();
            if (sideness == Sideness.RIGHT) return block.rotateYCCW();
            if (sideness == Sideness.LEFT) return block.rotateY();
        }
        return Direction.NORTH;
    }

    public enum Sideness {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM,
    }
}

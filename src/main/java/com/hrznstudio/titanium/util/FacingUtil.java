/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.util.EnumFacing;

public class FacingUtil {

    public static Sideness getFacingRelative(EnumFacing relative, EnumFacing facing) {
        if (facing == EnumFacing.UP) return Sideness.TOP;
        if (facing == EnumFacing.DOWN) return Sideness.BOTTOM;
        if (relative == facing) return Sideness.FRONT;
        if (relative == facing.getOpposite()) return Sideness.BACK;
        if (relative == facing.rotateY()) return Sideness.RIGHT;
        if (relative == facing.rotateYCCW()) return Sideness.LEFT;
        return Sideness.BOTTOM;
    }

    public enum Sideness {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM,
    }
}

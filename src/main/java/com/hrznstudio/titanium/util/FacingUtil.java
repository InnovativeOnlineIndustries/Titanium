/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
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

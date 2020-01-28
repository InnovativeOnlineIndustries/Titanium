/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

import javax.annotation.Nullable;

public class FacingUtil {

    public static void rotateBlock(Block block, Direction facing, Sideness sideness) {

    }

    public static Sideness getFacingRelative(Direction relative, @Nullable Direction facing) {
        if (facing == null) {
            return null;
        }
        if (facing == Direction.UP) {
            return Sideness.TOP;
        }
        if (facing == Direction.DOWN) {
            return Sideness.BOTTOM;
        }
        if (relative == facing) {
            return Sideness.FRONT;
        }
        if (relative == facing.getOpposite()) {
            return Sideness.BACK;
        }
        if (relative == facing.rotateYCCW()) {
            return Sideness.RIGHT;
        }
        if (relative == facing.rotateY()) {
            return Sideness.LEFT;
        }
        return Sideness.BOTTOM;
    }

    public static Direction getFacingFromSide(Direction block, Sideness sideness) {
        if (block.getAxis().isHorizontal()) {
            if (sideness == Sideness.TOP) {
                return Direction.UP;
            }
            if (sideness == Sideness.BOTTOM) {
                return Direction.DOWN;
            }
            if (sideness == Sideness.FRONT) {
                return block;
            }
            if (sideness == Sideness.BACK) {
                return block.getOpposite();
            }
            if (sideness == Sideness.LEFT) {
                return block.rotateYCCW();
            }
            if (sideness == Sideness.RIGHT) {
                return block.rotateY();
            }
        }
        return Direction.NORTH;
    }

    /**
     * Big shout-outs and credits to Immersive Engineering for providing most of the inspiration and example implementation code for us to figure out this implementation.
     * <p>
     * IE is licensed under "Blu's License of Common Sense" as seen here:
     * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE
     * <p>
     * You should also go check out their github repo:
     * https://github.com/BluSunrize/ImmersiveEngineering
     */
    public static Rotation getRotationBetweenFacings(Direction origin, Direction to) {

        if (to == origin) {
            return Rotation.NONE;
        }
        if (origin.getAxis() == Direction.Axis.Y || to.getAxis() == Direction.Axis.Y) {
            return null;
        }

        origin = origin.rotateY();
        if (origin == to) {
            return Rotation.CLOCKWISE_90;
        }
        origin = origin.rotateY();
        if (origin == to) {
            return Rotation.CLOCKWISE_180;
        }
        origin = origin.rotateY();
        if (origin == to) {
            return Rotation.COUNTERCLOCKWISE_90;
        }

        return null;//This shouldn't ever happen
    }

    public enum Sideness {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM,
    }
}

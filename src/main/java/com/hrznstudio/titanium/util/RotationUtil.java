package com.hrznstudio.titanium.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

public class RotationUtil {
    public static Rotation getRotationBetweenFacings(Direction origin, Direction to) {

        if(to == origin) {
            return Rotation.NONE;
        }
        if(origin.getAxis() == Direction.Axis.Y || to.getAxis() == Direction.Axis.Y) {
            return null;
        }

        origin = origin.rotateY();
        if(origin == to) {
            return Rotation.CLOCKWISE_90;
        }
        origin = origin.rotateY();
        if(origin == to) {
            return Rotation.CLOCKWISE_180;
        }
        origin = origin.rotateY();
        if(origin == to) {
            return Rotation.COUNTERCLOCKWISE_90;
        }

        return null;//This shouldn't ever happen
    }
}

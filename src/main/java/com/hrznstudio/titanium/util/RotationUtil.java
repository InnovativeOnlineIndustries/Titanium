package com.hrznstudio.titanium.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

/**
 * Big shout-outs and credits to Immersive Engineering for providing most of the inspiration and example implementation code for us to figure out this implementation.
 *
 * IE is licensed under "Blu's License of Common Sense" as seen here:
 * @Link https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE
 *
 * You should also go check out their github repo:
 * @Link https://github.com/BluSunrize/ImmersiveEngineering
 */
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

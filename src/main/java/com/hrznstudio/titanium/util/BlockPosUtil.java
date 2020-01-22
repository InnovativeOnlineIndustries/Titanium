package com.hrznstudio.titanium.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

/**
 * Big shout-outs and credits to Immersive Engineering for providing most of the inspiration and example implementation code for us to figure out this implementation.
 *
 * IE is licensed under "Blu's License of Common Sense" as seen here:
 * @Link https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE
 *
 * You should also go check out their github repo:
 * @Link https://github.com/BluSunrize/ImmersiveEngineering
 */
public class BlockPosUtil {
    public static BlockPos withSettingsAndOffset(BlockPos origin, BlockPos relative, Mirror mirror, Rotation rot) {
        PlacementSettings settings = new PlacementSettings().setMirror(mirror).setRotation(rot);
        return origin.add(Template.transformedBlockPos(settings, relative));
    }

    public static BlockPos withSettingsAndOffset(BlockPos origin, BlockPos relative, boolean mirrored, Direction facing) {
        Rotation rot = RotationUtil.getRotationBetweenFacings(Direction.NORTH, facing);
        if(rot == null) {
            return origin;
        }
        return withSettingsAndOffset(origin, relative, mirrored ? Mirror.FRONT_BACK : Mirror.NONE, rot);
    }
}

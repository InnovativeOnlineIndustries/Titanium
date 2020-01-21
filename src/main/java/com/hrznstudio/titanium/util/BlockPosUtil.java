package com.hrznstudio.titanium.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

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

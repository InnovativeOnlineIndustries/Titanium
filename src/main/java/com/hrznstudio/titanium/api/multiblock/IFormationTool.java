package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;

public interface IFormationTool {
    boolean targetFormationSide(Direction direction, PlayerEntity player, Vec3d vec3d);
}

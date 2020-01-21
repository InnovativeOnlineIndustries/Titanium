package com.hrznstudio.titanium.api.multiblock.matcher;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IMatch {
    ResultHandler matches(BlockState expected, BlockState found, @Nullable World world, @Nullable BlockPos pos);
}

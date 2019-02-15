package com.hrznstudio.titanium.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;

public interface RotationHandler {
    IBlockState getStateForPlacement(Block block, BlockItemUseContext context);
}

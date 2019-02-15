package com.hrznstudio.titanium.api.internal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;

import javax.annotation.Nullable;

public interface IColorProvider {
    int getColor(ItemStack var1, int var2);
    int getColor(IBlockState var1, @Nullable IWorldReaderBase var2, @Nullable BlockPos var3, int var4);
}

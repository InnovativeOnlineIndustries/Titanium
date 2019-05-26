/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.proton.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;

import javax.annotation.Nullable;

public interface IColorProvider {
    int getColor(ItemStack var1, int var2);

    int getColor(IBlockState var1, @Nullable IWorldReaderBase var2, @Nullable BlockPos var3, int var4);
}

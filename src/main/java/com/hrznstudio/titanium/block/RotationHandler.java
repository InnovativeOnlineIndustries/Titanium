/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;

public interface RotationHandler {
    BlockState getStateForPlacement(Block block, BlockItemUseContext context);
}

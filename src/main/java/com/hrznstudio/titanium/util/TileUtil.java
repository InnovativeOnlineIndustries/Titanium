/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;


public class TileUtil {
    public static <T extends BlockEntity> Optional<T> getTileEntity(BlockGetter access, BlockPos pos, Class<T> tileClass) {
        BlockEntity tile = access.getBlockEntity(pos);
        if (tileClass.isInstance(tile))
            return Optional.of(tileClass.cast(tile));
        return Optional.empty();
    }

    public static Optional<BlockEntity> getTileEntity(BlockGetter access, BlockPos blockPos) {
        return Optional.ofNullable(access.getBlockEntity(blockPos));
    }
}

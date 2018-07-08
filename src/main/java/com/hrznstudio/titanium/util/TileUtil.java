/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Optional;

public class TileUtil {
    public static <T extends TileEntity> Optional<T> getTileEntity(IBlockAccess access, BlockPos pos, Class<T> tileClass) {
        TileEntity tile = access.getTileEntity(pos);
        if (tileClass.isInstance(tile))
            return Optional.of(tileClass.cast(tile));
        return Optional.empty();
    }
}

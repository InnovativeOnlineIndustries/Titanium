/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.Optional;

public class TileUtil {
    public static <T extends TileEntity> Optional<T> getTileEntity(IBlockReader access, BlockPos pos, Class<T> tileClass) {
        TileEntity tile = access.getTileEntity(pos);
        if (tileClass.isInstance(tile))
            return Optional.of(tileClass.cast(tile));
        return Optional.empty();
    }
}

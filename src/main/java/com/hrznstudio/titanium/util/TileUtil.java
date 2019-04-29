/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Optional;

public class TileUtil {
    public static <T extends TileEntity> Optional<T> getTileEntity(IWorldReader access, BlockPos pos, Class<T> tileClass) {
        TileEntity tile = access.getTileEntity(pos);
        if (tileClass.isInstance(tile))
            return Optional.of(tileClass.cast(tile));
        return Optional.empty();
    }
}

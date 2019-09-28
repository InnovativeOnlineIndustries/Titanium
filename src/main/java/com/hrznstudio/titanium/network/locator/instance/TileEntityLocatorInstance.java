/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class TileEntityLocatorInstance extends LocatorInstance {
    private BlockPos blockPos;

    public TileEntityLocatorInstance(BlockPos blockPos) {
        super(LocatorTypes.TILE_ENTITY);
        this.blockPos = blockPos;
    }

    public TileEntityLocatorInstance() {
        super(LocatorTypes.TILE_ENTITY);
    }

    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return TileUtil.getTileEntity(playerEntity.getEntityWorld(), blockPos);
    }
}

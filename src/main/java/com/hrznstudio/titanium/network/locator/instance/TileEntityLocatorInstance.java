/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

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
    public Optional<?> locale(Player playerEntity) {
        return TileUtil.getTileEntity(playerEntity.getLevel(), blockPos);
    }

    @Override
    public ContainerLevelAccess getWorldPosCallable(Level world) {
        return ContainerLevelAccess.create(world, this.blockPos);
    }
}

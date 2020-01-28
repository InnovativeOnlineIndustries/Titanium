/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.multiblock.tile.MultiblockControllerTile;
import com.hrznstudio.titanium.multiblock.tile.MultiblockFillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockFillerBlock<T extends MultiblockFillerTile<T>> extends RotatableBlock<T> {

    public MultiblockFillerBlock(Properties properties, Class<T> tileClass) {
        super(properties, tileClass);
    }

    @Override
    public IFactory<T> getTileEntityFactory() {
        return null;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof IMultiblockComponent){
            if(((IMultiblockComponent) tile).isFormed()){
                BlockPos masterPos = ((IMultiblockComponent) tile).getMasterPosition();
                TileEntity master = world.getTileEntity(masterPos);
                if(master instanceof MultiblockControllerTile){
                    if(((MultiblockControllerTile) master).isFormed()) {
                        ((MultiblockControllerTile) master).onBreak();
                        ((MultiblockControllerTile) master).setFormed(false);
                    }
                }
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

}

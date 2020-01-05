/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative.tile;

import com.hrznstudio.titanium._impl.creative.BlockCreativeFEGenerator;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.energy.NBTEnergyHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileCreativeFEGenerator extends TilePowered {

    public TileCreativeFEGenerator() {
        super(BlockCreativeFEGenerator.INSTANCE);
    }

    @Override
    protected IFactory<NBTEnergyHandler> getEnergyHandlerFactory() {
        return () -> new NBTEnergyHandler(this, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            this.getEnergyStorage().receiveEnergy(Integer.MAX_VALUE, false);
            for (Direction direction : Direction.values()) {
                TileEntity tile = this.world.getTileEntity(this.getPos().offset(direction));
                if (tile != null)
                    tile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(iEnergyStorage -> iEnergyStorage.receiveEnergy(Integer.MAX_VALUE, false));
            }
            markForUpdate();
        }
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (!super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ)) {
            openGui(playerIn);
            return true;
        }
        return false;
    }
}

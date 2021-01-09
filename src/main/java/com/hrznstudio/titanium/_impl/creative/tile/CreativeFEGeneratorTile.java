/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative.tile;

import com.hrznstudio.titanium._impl.creative.CreativeFEGeneratorBlock;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class CreativeFEGeneratorTile extends PoweredTile<CreativeFEGeneratorTile> {

    public CreativeFEGeneratorTile() {
        super(CreativeFEGeneratorBlock.INSTANCE);
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
    @Nonnull
    public CreativeFEGeneratorTile getSelf() {
        return this;
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, facing, hitX, hitY, hitZ) == ActionResultType.PASS) {
            openGui(player);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    public EnergyStorageComponent<CreativeFEGeneratorTile> createEnergyStorage() {
        return new EnergyStorageComponent<>(Integer.MAX_VALUE, 0, 0);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative.tile;

import com.hrznstudio.titanium._impl.creative.CreativeFEGeneratorBlock;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class CreativeFEGeneratorTile extends PoweredTile<CreativeFEGeneratorTile> {

    public CreativeFEGeneratorTile(BlockPos pos, BlockState state) {
        super((BasicTileBlock<CreativeFEGeneratorTile>) CreativeFEGeneratorBlock.INSTANCE.getLeft().get(),CreativeFEGeneratorBlock.INSTANCE.getRight().get(), pos, state);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, CreativeFEGeneratorTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        this.getEnergyStorage().receiveEnergy(Integer.MAX_VALUE, false);
        for (Direction direction : Direction.values()) {
            BlockEntity tile = this.level.getBlockEntity(this.getBlockPos().relative(direction));
            if (tile != null)
                tile.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(iEnergyStorage -> iEnergyStorage.receiveEnergy(Integer.MAX_VALUE, false));
        }
        markForUpdate();
    }

    @Override
    @Nonnull
    public CreativeFEGeneratorTile getSelf() {
        return this;
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, facing, hitX, hitY, hitZ) == InteractionResult.PASS) {
            openGui(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public EnergyStorageComponent<CreativeFEGeneratorTile> createEnergyStorage() {
        return new EnergyStorageComponent<>(Integer.MAX_VALUE, 0, 0);
    }
}

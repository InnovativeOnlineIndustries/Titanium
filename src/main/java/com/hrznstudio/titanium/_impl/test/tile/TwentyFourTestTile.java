/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.TwentyFourTestBlock;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TwentyFourTestTile extends PoweredTile<TwentyFourTestTile> {
    @Save
    private ProgressBarComponent<TwentyFourTestTile> bar;
    @Save
    private InventoryComponent<TwentyFourTestTile> first;
    @Save
    private InventoryComponent<TwentyFourTestTile> second;
    @Save
    private InventoryComponent<TwentyFourTestTile> third;

    public TwentyFourTestTile() {
        super(TwentyFourTestBlock.TEST);

        this.addInventory(first = new InventoryComponent<TwentyFourTestTile>("test", -120, 20, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addInventory(second = new InventoryComponent<TwentyFourTestTile>("test2", 80, 30, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addProgressBar(bar = new ProgressBarComponent<TwentyFourTestTile>(20, 20, 500)
                .setCanIncrease(componentHarness -> true)
                .setOnFinishWork(() -> System.out.println("WOWOOW")));
        this.addInventory(third = new InventoryComponent<TwentyFourTestTile>("test3", 180, 30, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        openGui(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, TwentyFourTestTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        this.getEnergyStorage().receiveEnergy(10, false);
        markForUpdate();
    }

    @Override
    @Nonnull
    public TwentyFourTestTile getSelf() {
        return this;
    }
}

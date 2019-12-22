/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.BlockTwentyFourTest;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.ParametersAreNonnullByDefault;

public class TileTwentyFourTest extends TilePowered<TileTwentyFourTest> implements ITickableTileEntity {

    @Save
    private ProgressBarComponent<TileTwentyFourTest> bar;
    @Save
    private InventoryComponent<TileTwentyFourTest> first;
    @Save
    private InventoryComponent<TileTwentyFourTest> second;
    @Save
    private InventoryComponent<TileTwentyFourTest> third;

    public TileTwentyFourTest() {
        super(BlockTwentyFourTest.TEST);

        this.addInventory(first = new InventoryComponent<TileTwentyFourTest>("test", -120, 20, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addInventory(second = new InventoryComponent<TileTwentyFourTest>("test2", 80, 30, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new ProgressBarComponent<TileTwentyFourTest>(20, 20, 500)
                .setCanIncrease(componentHarness -> true)
                .setOnFinishWork(() -> System.out.println("WOWOOW")));
        this.addInventory(third = new InventoryComponent<TileTwentyFourTest>("test3", 180, 30, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        openGui(player);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            this.getEnergyStorage().receiveEnergy(10, false);
            markForUpdate();
        }
    }

    @Override
    public TileTwentyFourTest getSelf() {
        return this;
    }
}

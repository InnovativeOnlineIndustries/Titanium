/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
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
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.ParametersAreNonnullByDefault;

public class TwentyFourTestTile extends PoweredTile<TwentyFourTestTile> implements ITickableTileEntity {

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
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new ProgressBarComponent<TwentyFourTestTile>(20, 20, 500)
                .setCanIncrease(componentHarness -> true)
                .setOnFinishWork(() -> System.out.println("WOWOOW")));
        this.addInventory(third = new InventoryComponent<TwentyFourTestTile>("test3", 180, 30, 1)
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
    public TwentyFourTestTile getSelf() {
        return this;
    }
}

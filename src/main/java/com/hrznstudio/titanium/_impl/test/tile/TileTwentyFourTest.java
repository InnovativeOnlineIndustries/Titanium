/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.BlockTwentyFourTest;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

public class TileTwentyFourTest extends TilePowered implements ITickableTileEntity {

    @Save
    private PosProgressBar bar;

    @Save
    private PosInvHandler first;
    @Save
    private PosInvHandler second;
    @Save
    private PosInvHandler third;

    public TileTwentyFourTest() {
        super(BlockTwentyFourTest.TEST);

        this.addInventory(first = new PosInvHandler("test", -120, 20, 1).setTile(this).setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addInventory(second = new PosInvHandler("test2", 80, 30, 1).setTile(this).setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new PosProgressBar(20, 20, 500).setCanIncrease(tileEntity -> true).setOnFinishWork(() -> System.out.println("WOWOOW")));
        this.addInventory(third = new PosInvHandler("test3", 180, 30, 1).setTile(this).setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
    }

    @Override
    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        openGui(playerIn);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            this.getEnergyStorage().receiveEnergy(10, false);
            markForUpdate();
        } else {

        }
    }
}

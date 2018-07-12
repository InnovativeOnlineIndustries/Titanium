/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

public class TileTest extends TilePowered implements ITickable {

    @Save
    private PosProgressBar bar;

    public TileTest() {
//        this.addInventory(new PosInvHandler("test", 20, 20, 3).setTile(this).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
//        this.addInventory(new PosInvHandler("test2", 80, 30, 6).setTile(this).setRange(3, 2).setInputFilter((stack, integer) -> stack.isItemEqual(new ItemStack(Blocks.COBBLESTONE))));
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new PosProgressBar(20, 20, 500).setCanIncrease(tileEntity -> true).setWork(() -> System.out.println("WOWOOW")));
    }

    @Override
    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            openGui(playerIn);
        }
        return true;
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            this.getEnergyStorage().receiveEnergy(10, false);
            markForUpdate();
        } else {

        }
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.BlockAssetTest;
import com.hrznstudio.titanium._impl.test.BlockTest;
import com.hrznstudio.titanium._impl.test.assetsystem.NewAssetProviderTest;
import com.hrznstudio.titanium._impl.test.assetsystem.TestAssetProvider;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.block.tile.fluid.SidedFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.addon.AssetGuiAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class TileAssetTest extends TilePowered implements ITickableTileEntity {
    @Save
    private SidedInvHandler inventory;
    @Save
    private PosInvHandler recipe;
    @Save
    private PosInvHandler fakeOutput;
    @Save
    private SidedInvHandler realOutput;
    @Save
    private PosInvHandler randomSlot;
    @Save
    private SidedInvHandler fluidInput;
    @Save
    private PosProgressBar progressBar;
    @Save
    private SidedFluidTank fluidTank;

    public TileAssetTest() {
        super(BlockAssetTest.TEST);
        this.addInventory(inventory = (SidedInvHandler) new SidedInvHandler("inventory", 8, 88, 18, 0).setButtonCoords(-15, 1).setRange(9, 2).setTile(this).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(recipe = new PosInvHandler("recipe", 10, 20, 9).setRange(3, 3).setTile(this).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(fakeOutput = new PosInvHandler("fake_output", 80, 51, 1).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(realOutput = (SidedInvHandler) new SidedInvHandler("real_output", 120, 56, 1, 0).setButtonCoords(-15, 16).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(randomSlot = new PosInvHandler("random_slot", 83, 20, 1).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(fluidInput = (SidedInvHandler) new SidedInvHandler("fluid_input", 119, 20, 1, 0).setButtonCoords(-15, 31).setSlotLimit(1).setTile(this));
        this.addProgressBar(progressBar = new PosProgressBar(98, 50, 500).setCanIncrease(tileEntity -> true).setBarDirection(PosProgressBar.BarDirection.VERTICAL_UP));
        this.addTank(fluidTank = (SidedFluidTank) new SidedFluidTank("fluid" , 16000, 150, 17, 0).setButtonCoords(-15, 46).setTile(this));
        inventory.setColor(DyeColor.CYAN);
        realOutput.setColor(DyeColor.RED);
        fluidInput.setColor(DyeColor.LIGHT_BLUE);
        fluidTank.setColor(DyeColor.BLUE);
    }

    @Override
    public void tick() {
        progressBar.tickBar();
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (!super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ)) {
            openGui(playerIn);
            return true;
        }
        return false;
    }

    @Override
    public IAssetProvider getAssetProvider() {
        return NewAssetProviderTest.TEST_PROVIDER;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.BlockAssetTest;
import com.hrznstudio.titanium._impl.test.assetsystem.NewAssetProviderTest;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TileAssetTest extends TilePowered<TileAssetTest> implements ITickableTileEntity {
    @Save
    private SidedInventoryComponent<TileAssetTest> inventory;
    @Save
    private InventoryComponent<TileAssetTest> recipe;
    @Save
    private InventoryComponent<TileAssetTest> fakeOutput;
    @Save
    private SidedInventoryComponent<TileAssetTest> realOutput;
    @Save
    private InventoryComponent<TileAssetTest> randomSlot;
    @Save
    private SidedInventoryComponent<TileAssetTest> fluidInput;
    @Save
    private ProgressBarComponent<TileAssetTest> progressBar;
    @Save
    private SidedFluidTankComponent<TileAssetTest> fluidTank;

    public TileAssetTest() {
        super(BlockAssetTest.TEST);
        this.addInventory(inventory = (SidedInventoryComponent<TileAssetTest>) new SidedInventoryComponent<TileAssetTest>("inventory", 8, 88, 18, 0)
                .setFacingHandlerPos(-15, 1).setRange(9, 2)
                .setComponentHarness(this)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(recipe = new InventoryComponent<TileAssetTest>("recipe", 10, 20, 9)
                .setRange(3, 3)
                .setComponentHarness(this)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(fakeOutput = new InventoryComponent<TileAssetTest>("fake_output", 80, 51, 1)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(realOutput = (SidedInventoryComponent<TileAssetTest>) new SidedInventoryComponent<TileAssetTest>("real_output", 120, 56, 1, 0)
                .setFacingHandlerPos(-15, 16)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(randomSlot = new InventoryComponent<TileAssetTest>("random_slot", 83, 20, 1)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(fluidInput = (SidedInventoryComponent<TileAssetTest>) new SidedInventoryComponent<TileAssetTest>("fluid_input", 119, 20, 1, 0)
                .setFacingHandlerPos(-15, 31)
                .setSlotLimit(1)
                .setComponentHarness(this));
        this.addProgressBar(progressBar = new ProgressBarComponent<TileAssetTest>(98, 50, 500)
                .setCanIncrease(tileEntity -> true)
                .setBarDirection(ProgressBarComponent.BarDirection.VERTICAL_UP));
        this.addTank(fluidTank = (SidedFluidTankComponent<TileAssetTest>) new SidedFluidTankComponent<TileAssetTest>("fluid", 16000, 150, 17, 0)
                .setFacingHandlerPos(-15, 46)
                .setComponentHarness(this));
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
    @Nonnull
    public TileAssetTest getSelf() {
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

    @Override
    public IAssetProvider getAssetProvider() {
        return NewAssetProviderTest.TEST_PROVIDER;
    }
}

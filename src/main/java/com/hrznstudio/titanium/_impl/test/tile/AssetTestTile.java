/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.AssetTestBlock;
import com.hrznstudio.titanium._impl.test.assetsystem.NewAssetProviderTest;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class AssetTestTile extends PoweredTile<AssetTestTile>{
    @Save
    private SidedInventoryComponent<AssetTestTile> inventory;
    @Save
    private InventoryComponent<AssetTestTile> recipe;
    @Save
    private InventoryComponent<AssetTestTile> fakeOutput;
    @Save
    private SidedInventoryComponent<AssetTestTile> realOutput;
    @Save
    private InventoryComponent<AssetTestTile> randomSlot;
    @Save
    private SidedInventoryComponent<AssetTestTile> fluidInput;
    @Save
    private ProgressBarComponent<AssetTestTile> progressBar;
    @Save
    private SidedFluidTankComponent<AssetTestTile> fluidTank;

    public AssetTestTile(BlockPos pos, BlockState state) {
        super((BasicTileBlock<AssetTestTile>) AssetTestBlock.TEST.get(), pos, state);
        this.addInventory(inventory = (SidedInventoryComponent<AssetTestTile>) new SidedInventoryComponent<AssetTestTile>("inventory", 8, 88, 18, 0)
            .setFacingHandlerPos(-15, 1).setRange(9, 2)
            .setComponentHarness(this)
            .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(recipe = new InventoryComponent<AssetTestTile>("recipe", 10, 20, 9)
            .setRange(3, 3)
            .setComponentHarness(this)
            .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(fakeOutput = new InventoryComponent<AssetTestTile>("fake_output", 80, 51, 1)
            .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(realOutput = (SidedInventoryComponent<AssetTestTile>) new SidedInventoryComponent<AssetTestTile>("real_output", 120, 56, 1, 0)
                .setFacingHandlerPos(-15, 16)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(randomSlot = new InventoryComponent<AssetTestTile>("random_slot", 83, 20, 1)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(fluidInput = (SidedInventoryComponent<AssetTestTile>) new SidedInventoryComponent<AssetTestTile>("fluid_input", 119, 20, 1, 0)
                .setFacingHandlerPos(-15, 31)
                .setSlotLimit(1)
                .setComponentHarness(this));
        this.addProgressBar(progressBar = new ProgressBarComponent<AssetTestTile>(98, 50, 500)
                .setCanIncrease(tileEntity -> true)
                .setBarDirection(ProgressBarComponent.BarDirection.VERTICAL_UP));
        this.addTank(fluidTank = (SidedFluidTankComponent<AssetTestTile>) new SidedFluidTankComponent<AssetTestTile>("fluid", 16000, 150, 17, 0)
                .setFacingHandlerPos(-15, 46)
                .setComponentHarness(this));
        inventory.setColor(DyeColor.CYAN);
        realOutput.setColor(DyeColor.RED);
        fluidInput.setColor(DyeColor.LIGHT_BLUE);
        fluidTank.setColor(DyeColor.BLUE);
        this.setShowEnergy(false);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, AssetTestTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        progressBar.tickBar();
    }

    @Override
    @Nonnull
    public AssetTestTile getSelf() {
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

    @Override
    public IAssetProvider getAssetProvider() {
        return NewAssetProviderTest.TEST_PROVIDER;
    }

    @Nonnull
    @Override
    public EnergyStorageComponent<AssetTestTile> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000, 0, 0);
    }
}

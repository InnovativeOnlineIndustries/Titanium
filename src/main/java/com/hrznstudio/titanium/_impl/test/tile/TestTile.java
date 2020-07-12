/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.TestBlock;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.redstone.IRedstoneReader;
import com.hrznstudio.titanium.api.redstone.IRedstoneState;
import com.hrznstudio.titanium.block.redstone.RedstoneAction;
import com.hrznstudio.titanium.block.redstone.RedstoneManager;
import com.hrznstudio.titanium.block.redstone.RedstoneState;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonInfo;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.button.RedstoneControlButtonComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestTile extends PoweredTile<TestTile> implements IRedstoneReader {

    @Save
    private ProgressBarComponent<TestTile> bar;
    @Save
    private SidedInventoryComponent<TestTile> first;
    @Save
    private SidedInventoryComponent<TestTile> second;
    @Save
    private FluidTankComponent<TestTile> third;

    private ButtonComponent button;
    @Save
    private int state;
    @Save
    private RedstoneManager<RedstoneAction> redstoneManager;
    private RedstoneControlButtonComponent<RedstoneAction> redstoneButton;

    public TestTile() {
        super(TestBlock.TEST);
        this.addInventory(first = (SidedInventoryComponent<TestTile>) new SidedInventoryComponent<TestTile>("test", 80, 30, 1, 0)
            .setComponentHarness(this)
            .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(second = (SidedInventoryComponent<TestTile>) new SidedInventoryComponent<TestTile>("test2", 80, 60, 2, 1)
            .setComponentHarness(this)
            .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack))
            .setSlotToItemStackRender(0, new ItemStack(Items.STONE_PICKAXE))
                .setSlotToColorRender(1, DyeColor.ORANGE));
        this.addGuiAddonFactory(() -> new EnergyBarScreenAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new ProgressBarComponent<TestTile>(40, 20, 500)
            .setCanIncrease(tileEntity -> redstoneManager.getAction().canRun(tileEntity.getEnvironmentValue(false, null)) && redstoneManager.shouldWork())
            .setOnFinishWork(() -> {
                redstoneManager.finish();
                System.out.println(redstoneManager.getAction());
                System.out.println("WOWOOW");
            })
                .setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT)
            .setCanReset(testTile -> bar.getProgress() >= bar.getMaxProgress())
                .setColor(DyeColor.LIME));
        this.addTank(third = new FluidTankComponent<>("testTank", 8000, 130, 30));
        this.addButton(button = new ButtonComponent(-13, 1, 14, 14) {
            @Override
            public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
                return Collections.singletonList(() -> new StateButtonAddon(button, new StateButtonInfo(0, AssetTypes.BUTTON_SIDENESS_DISABLED), new StateButtonInfo(1, AssetTypes.BUTTON_SIDENESS_ENABLED), new StateButtonInfo(2, AssetTypes.BUTTON_SIDENESS_PULL), new StateButtonInfo(3, AssetTypes.BUTTON_SIDENESS_PUSH)) {
                    @Override
                    public int getState() {
                        return state;
                    }
                });
            }
        }.setId(0).setPredicate((playerEntity, compoundNBT) -> {
            System.out.println(":pepeD:");
            ++state;
            System.out.println(getEnvironmentValue(false, null));
            System.out.println(getEnvironmentValue(true, FacingUtil.getFacingFromSide(this.getFacingDirection(), FacingUtil.Sideness.TOP)));
            if (state >= 4) state = 0;
            markForUpdate();
        }));
        redstoneButton = new RedstoneControlButtonComponent<>(152, 84, 14, 14, () -> this.redstoneManager, () -> this);
        redstoneButton.setId(1);
        this.addButton(redstoneButton);
        first.setColor(DyeColor.LIME);
        second.setColor(DyeColor.CYAN);
        this.redstoneManager = new RedstoneManager<>(RedstoneAction.IGNORE, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (Objects.requireNonNull(getWorld()).isRaining()) {
            getWorld().getWorldInfo().setRaining(false);
        }
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {
        super.onNeighborChanged(blockIn, fromPos);
        redstoneManager.setLastRedstoneState(this.getEnvironmentValue(false, null).isReceivingRedstone());
    }

    @Override
    @Nonnull
    public TestTile getSelf() {
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
    public IRedstoneState getEnvironmentValue(boolean strongPower, Direction direction) {
        if (strongPower) {
            if (direction == null) {
                return this.world.isBlockPowered(this.pos) ? RedstoneState.ON : RedstoneState.OFF;
            }
            return this.world.isSidePowered(this.pos, direction) ? RedstoneState.ON : RedstoneState.OFF;
        } else {
            return this.world.getRedstonePowerFromNeighbors(this.pos) > 0 ? RedstoneState.ON : RedstoneState.OFF;
        }
    }

}

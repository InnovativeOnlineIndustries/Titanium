/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.BlockTest;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TileTest extends TilePowered<TileTest> {

    @Save
    private ProgressBarComponent<TileTest> bar;
    @Save
    private SidedInventoryComponent<TileTest> first;
    @Save
    private SidedInventoryComponent<TileTest> second;
    @Save
    private FluidTankComponent<TileTest> third;

    private ButtonComponent button;
    @Save
    private int state;

    public TileTest() {
        super(BlockTest.TEST);
        this.addInventory(first = (SidedInventoryComponent<TileTest>) new SidedInventoryComponent<TileTest>("test", 80, 60, 1, 0)
                .setComponentHarness(this)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(second = (SidedInventoryComponent<TileTest>) new SidedInventoryComponent<TileTest>("test2", 80, 30, 1, 1)
                .setComponentHarness(this)
                .setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new ProgressBarComponent<TileTest>(40, 20, 500)
                .setCanIncrease(tileEntity -> true)
                .setOnFinishWork(() -> System.out.println("WOWOOW"))
                .setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT)
                .setColor(DyeColor.LIME));
        this.addTank(third = new FluidTankComponent<>("testTank", 8000, 130, 30));
        this.addButton(button = new ButtonComponent(-13, 1, 14, 14) {
            @Override
            public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
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
            if (state >= 4) state = 0;
            markForUpdate();
        }));
        first.setColor(DyeColor.LIME);
        second.setColor(DyeColor.CYAN);
    }

    @Override
    public void tick() {
        bar.tickBar();
        if (Objects.requireNonNull(getWorld()).isRaining()) {
            getWorld().getWorldInfo().setRaining(false);
        }
    }

    @Override
    @Nonnull
    public TileTest getSelf() {
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

}

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
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonInfo;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestTile extends PoweredTile<TestTile> {

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
                .setCanIncrease(tileEntity -> true)
                .setOnFinishWork(() -> System.out.println("WOWOOW"))
                .setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT)
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
            System.out.println(getEnvironmentValue(false, FacingUtil.Sideness.TOP));
            System.out.println(getEnvironmentValue(true, FacingUtil.Sideness.TOP));
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
}

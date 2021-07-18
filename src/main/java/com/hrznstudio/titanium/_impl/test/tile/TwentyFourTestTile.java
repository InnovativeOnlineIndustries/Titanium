/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.TwentyFourTestBlock;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.WidgetScreenAddon;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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

        this.addInventory(first = new InventoryComponent<TwentyFourTestTile>("test", 80, 20, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addInventory(second = new InventoryComponent<TwentyFourTestTile>("test2", 80, 40, 1)
                .setComponentHarness(this)
                .setInputFilter(IItemStackQuery.ANYTHING.toSlotFilter()));
        this.addProgressBar(bar = new ProgressBarComponent<TwentyFourTestTile>(110, 20, 500)
                .setCanIncrease(componentHarness -> true)
                .setOnFinishWork(() -> System.out.println("WOWOOW")));
        this.addInventory(third = new InventoryComponent<TwentyFourTestTile>("test3", 80, 60, 1)
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

    @Nonnull
    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = super.getScreenAddons();
        for (IFactory<? extends IScreenAddon> addon : addons) {
            if (addon.create() instanceof EnergyBarScreenAddon) {
                addons.remove(addon);
                addons.add(() -> new EnergyBarScreenAddon(50, 20, this.getEnergyStorage()));
            }
        }
        TextFieldWidget widget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 120, 20, new StringTextComponent(""));
        widget.setText("This is a Text Widget");
        addons.add(() -> new WidgetScreenAddon(30, -25, widget));
        return addons;
    }

    @Override
    @Nonnull
    public TwentyFourTestTile getSelf() {
        return this;
    }
}

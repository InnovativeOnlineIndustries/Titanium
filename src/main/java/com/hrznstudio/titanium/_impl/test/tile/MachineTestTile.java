/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.MachineTestBlock;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.filter.FilterSlot;
import com.hrznstudio.titanium.block.tile.MachineTile;
import com.hrznstudio.titanium.component.bundle.TankInteractionBundle;
import com.hrznstudio.titanium.filter.ItemStackFilter;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;

public class MachineTestTile extends MachineTile<MachineTestTile> {
    @Save
    private ItemStackFilter filter;
    @Save
    private TankInteractionBundle<MachineTestTile> tankBundle;

    public MachineTestTile() {
        super(MachineTestBlock.TEST);
        addFilter(this.filter = new ItemStackFilter("filter", 12));
        int pos = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                FilterSlot slot = new FilterSlot<>(20 + x * 18, 20 + y * 18, pos, ItemStack.EMPTY);
                slot.setColor(DyeColor.CYAN);
                this.filter.setFilter(pos, slot);
                ++pos;
            }
        }
        this.addBundle(tankBundle = new TankInteractionBundle<>(() -> this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY), 175, 94, this, 10));
    }

    @Nonnull
    @Override
    public MachineTestTile getSelf() {
        return this;
    }
}

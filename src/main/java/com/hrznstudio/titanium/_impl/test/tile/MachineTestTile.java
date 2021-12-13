/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.tile;

import com.hrznstudio.titanium._impl.test.MachineTestBlock;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.filter.FilterSlot;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.MachineTile;
import com.hrznstudio.titanium.component.bundle.TankInteractionBundle;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.filter.ItemStackFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class MachineTestTile extends MachineTile<MachineTestTile> {
    @Save
    private ItemStackFilter filter;
    @Save
    private TankInteractionBundle<MachineTestTile> tankBundle;
    @Save
    private InventoryComponent<MachineTestTile> movingSlot;

    public MachineTestTile(BlockPos blockPos, BlockState state) {
        super((BasicTileBlock<MachineTestTile>) MachineTestBlock.TEST.get(), blockPos, state);
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
        this.setShowEnergy(false);
        this.addBundle(tankBundle = new TankInteractionBundle<>(() -> this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY), 175, 94, this, 10));
        this.addInventory(this.movingSlot = new InventoryComponent<MachineTestTile>("moving_slot", 0, 0, 1).setInputFilter((stack, integer) -> this.movingSlot.getSlotVisiblePredicate().test(integer)).setSlotVisiblePredicate(integer -> this.level.getGameTime() % 100 > 40).setSlotPosition(integer -> Pair.of((int) this.level.getGameTime() % 100, 50 + (int) this.level.getGameTime() % 50 - 25)));
    }

    @Nonnull
    @Override
    public MachineTestTile getSelf() {
        return this;
    }
}

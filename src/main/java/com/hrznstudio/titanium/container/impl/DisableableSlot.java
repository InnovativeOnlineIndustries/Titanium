/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import com.hrznstudio.titanium.container.IDisableableContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.BooleanSupplier;

public class DisableableSlot extends Slot {
    private BooleanSupplier isDisabled;

    public DisableableSlot(Container inventoryIn, int index, int xPosition, int yPosition, IDisableableContainer disableableContainer) {
        this(inventoryIn, index, xPosition, yPosition, disableableContainer::isDisabled);
    }

    public DisableableSlot(Container inventoryIn, int index, int xPosition, int yPosition, BooleanSupplier isDisabled) {
        super(inventoryIn, index, xPosition, yPosition);
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean isActive() {
        return !isDisabled.getAsBoolean();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !isDisabled.getAsBoolean();
    }

    @Override
    public boolean mayPickup(Player player) {
        return !isDisabled.getAsBoolean();
    }

    public void setIsDisabled(BooleanSupplier isDisabled) {
        this.isDisabled = isDisabled;
    }
}

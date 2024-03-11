/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import com.hrznstudio.titanium.container.IDisableableContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.BooleanSupplier;

public class DisableableSlot extends Slot {
    private final BooleanSupplier isDisabled;

    public DisableableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, IDisableableContainer disableableContainer) {
        this(inventoryIn, index, xPosition, yPosition, disableableContainer::isDisabled);
    }

    public DisableableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, BooleanSupplier isDisabled) {
        super(inventoryIn, index, xPosition, yPosition);
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean isEnabled() {
        return !isDisabled.getAsBoolean();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !isDisabled.getAsBoolean();
    }
}

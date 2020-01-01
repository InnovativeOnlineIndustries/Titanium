/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class DisableableSlot extends Slot {

    private ContainerInventoryBase containerInventoryBase;

    public DisableableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, ContainerInventoryBase containerInventoryBase) {
        super(inventoryIn, index, xPosition, yPosition);
        this.containerInventoryBase = containerInventoryBase;
    }

    @Override
    public boolean isEnabled() {
        return !containerInventoryBase.isDisabled();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !containerInventoryBase.isDisabled();
    }
}

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

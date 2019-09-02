package com.hrznstudio.titanium.container.impl;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class DisableableItemHandlerSlot extends SlotItemHandler {

    private ContainerInventoryBase containerInventoryBase;

    public DisableableItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ContainerInventoryBase containerInventoryBase) {
        super(itemHandler, index, xPosition, yPosition);
        this.containerInventoryBase = containerInventoryBase;
    }

    @Override
    public boolean isEnabled() {
        return !containerInventoryBase.isDisabled();
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return !containerInventoryBase.isDisabled();
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class DisableableItemHandlerSlot extends SlotItemHandler {
    private final BooleanSupplier isDisabled;

    public DisableableItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, BasicInventoryContainer basicInventoryContainer) {
        this(itemHandler, index, xPosition, yPosition, basicInventoryContainer::isDisabled);
    }

    public DisableableItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, BooleanSupplier isDisabled) {
        super(itemHandler, index, xPosition, yPosition);
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean isEnabled() {
        return !isDisabled.getAsBoolean();
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return !isDisabled.getAsBoolean();
    }
}

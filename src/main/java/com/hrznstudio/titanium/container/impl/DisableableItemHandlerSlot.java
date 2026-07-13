/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class DisableableItemHandlerSlot extends ResourceHandlerSlot {
    private final BooleanSupplier isDisabled;

    public DisableableItemHandlerSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, BasicInventoryContainer basicInventoryContainer) {
        this(itemHandler, index, xPosition, yPosition, basicInventoryContainer::isDisabled);
    }

    public DisableableItemHandlerSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, BooleanSupplier isDisabled) {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean isActive() {
        return !isDisabled.getAsBoolean();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return !isDisabled.getAsBoolean();
    }
}

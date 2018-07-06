/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class InventoryUtil {
    @Nonnull
    public static List<ItemStack> getStacks(@Nonnull ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (handler == null)
            return Collections.emptyList();
        return getStacks(handler);
    }

    @Nonnull
    public static List<ItemStack> getStacks(@Nonnull TileEntity tile) {
        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (handler == null)
            return Collections.emptyList();
        return getStacks(handler);
    }

    @Nonnull
    public static List<ItemStack> getStacks(@Nonnull IItemHandler handler) {
        ImmutableList.Builder<ItemStack> builder = new ImmutableList.Builder<>();
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack subStack = handler.getStackInSlot(slot);
            if (!subStack.isEmpty())
                builder.add(subStack);
        }
        return builder.build();
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class InventoryUtil {
    public static List<ItemStack> getStacks(ICapabilityProvider provider) {
        LazyOptional<IItemHandler> inv = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (!inv.isPresent())
            return Collections.emptyList();
        return getStacks(inv.orElseThrow(NullPointerException::new));
    }

    public static List<ItemStack> getStacks(@Nullable IItemHandler handler) {
        if (handler == null)
            return Collections.emptyList();
        ImmutableList.Builder<ItemStack> builder = new ImmutableList.Builder<>();
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack subStack = handler.getStackInSlot(slot);
            if (!subStack.isEmpty())
                builder.add(subStack);
        }
        return builder.build();
    }
}
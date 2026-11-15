/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class InventoryUtil {
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

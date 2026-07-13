/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nonnull;


public class ItemHandlerUtil {

    @Nonnull
    public static ItemStack getFirstItem(ResourceHandler<ItemResource> handler) {
        for (int i = 0; i < handler.size(); i++) {
            if (!handler.getResource(i).isEmpty()) {
                return handler.getResource(i).toStack(handler.getAmountAsInt(i));
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isEmpty(ResourceHandler<ItemResource> handler) {
        for (int i = 0; i < handler.size(); i++) {
            if (!handler.getResource(i).isEmpty()) return false;
        }
        return true;
    }

}

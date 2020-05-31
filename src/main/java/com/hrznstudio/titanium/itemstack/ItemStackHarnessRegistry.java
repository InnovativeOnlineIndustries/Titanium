/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.itemstack;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ItemStackHarnessRegistry {
    private static final ItemStackHarnessRegistry INSTANCE = new ItemStackHarnessRegistry();

    private final Map<Item, Function<ItemStack, ItemStackHarness>> harnessCreators;

    public ItemStackHarnessRegistry() {
        harnessCreators = Maps.newHashMap();
    }


    public static void register(Item item, Function<ItemStack, ItemStackHarness> harnessCreator) {
        getHarnessCreators().put(item, harnessCreator);
    }

    public static Map<Item, Function<ItemStack, ItemStackHarness>> getHarnessCreators() {
        return getInstance().harnessCreators;
    }

    public static Optional<ItemStackHarness> createItemStackHarness(ItemStack itemStack) {
        return Optional.of(itemStack.getItem())
            .map(getHarnessCreators()::get)
            .map(harnessCreator -> harnessCreator.apply(itemStack));
    }

    public static ItemStackHarnessRegistry getInstance() {
        return INSTANCE;
    }
}

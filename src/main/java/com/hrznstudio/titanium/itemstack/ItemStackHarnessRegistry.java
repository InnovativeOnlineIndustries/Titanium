/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.itemstack;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemStackHarnessRegistry {
    private static final ItemStackHarnessRegistry INSTANCE = new ItemStackHarnessRegistry();

    private final Map<Supplier<Item>, Function<ItemStack, ItemStackHarness>> harnessCreators;

    public ItemStackHarnessRegistry() {
        harnessCreators = Maps.newHashMap();
    }


    public static void register(Supplier<Item> item, Function<ItemStack, ItemStackHarness> harnessCreator) {
        getHarnessCreators().put(item, harnessCreator);
    }

    public static Map<Supplier<Item>, Function<ItemStack, ItemStackHarness>> getHarnessCreators() {
        return getInstance().harnessCreators;
    }

    public static Optional<ItemStackHarness> createItemStackHarness(ItemStack itemStack) {
        for (Map.Entry<Supplier<Item>, Function<ItemStack, ItemStackHarness>> itemSupplier : getHarnessCreators().entrySet()) {
            if (itemSupplier.getKey().get().equals(itemStack.getItem())) {
                return Optional.of(itemSupplier.getValue().apply(itemStack));
            }
        }

        return Optional.empty();
    }

    public static ItemStackHarnessRegistry getInstance() {
        return INSTANCE;
    }
}

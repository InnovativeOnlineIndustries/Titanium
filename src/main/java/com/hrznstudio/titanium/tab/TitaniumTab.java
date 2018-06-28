/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class TitaniumTab extends CreativeTabs {
    protected Supplier<ItemStack> stackSupplier;

    public TitaniumTab(String label, Supplier<ItemStack> stackSupplier) {
        super(label);
        this.stackSupplier = stackSupplier;
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        return stackSupplier.get();
    }
}

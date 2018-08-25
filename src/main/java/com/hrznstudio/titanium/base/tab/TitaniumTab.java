/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.tab;

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

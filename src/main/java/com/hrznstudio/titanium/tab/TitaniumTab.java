/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.tab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class TitaniumTab extends ItemGroup {
    protected Supplier<ItemStack> stackSupplier;

    public TitaniumTab(String label, Supplier<ItemStack> stackSupplier) {
        super(label);
        this.stackSupplier = stackSupplier;
    }

    @Override
    public ItemStack createIcon() {
        return stackSupplier.get();
    }
}

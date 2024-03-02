/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.tab;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TitaniumTab extends CreativeModeTab {
    protected Supplier<ItemStack> stackSupplier;

    public TitaniumTab(String label, Supplier<ItemStack> stackSupplier) {
        super(label);
        this.stackSupplier = stackSupplier;
    }

    @Override
    public ItemStack makeIcon() {
        return stackSupplier.get();
    }

    @Override
    public ItemStack getIconItem() {
        return stackSupplier.get();
    }
}

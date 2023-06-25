/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.tab;

import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TitaniumTab {
    protected Supplier<ItemStack> stackSupplier;

    public TitaniumTab(String label, Supplier<ItemStack> stackSupplier) {

        this.stackSupplier = stackSupplier;
    }


    public ItemStack makeIcon() {
        return stackSupplier.get();
    }


    public ItemStack getIconItem() {
        return stackSupplier.get();
    }
}

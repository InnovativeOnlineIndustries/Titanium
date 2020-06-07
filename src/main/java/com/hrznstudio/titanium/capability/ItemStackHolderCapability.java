/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.api.capability.IStackHolder;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ItemStackHolderCapability implements IStackHolder {

    private Supplier<ItemStack> holder;

    public ItemStackHolderCapability(Supplier<ItemStack> holder) {
        this.holder = holder;
    }

    @Override
    public ItemStack getHolder() {
        return holder.get();
    }

    @Override
    public void setHolder(Supplier<ItemStack> stack) {
        this.holder = stack;
    }

}

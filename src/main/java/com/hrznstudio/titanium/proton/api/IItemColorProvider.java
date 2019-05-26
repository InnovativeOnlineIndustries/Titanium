/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.proton.api;

import net.minecraft.item.ItemStack;

public interface IItemColorProvider {
    int getColor(ItemStack var1, int var2);
}

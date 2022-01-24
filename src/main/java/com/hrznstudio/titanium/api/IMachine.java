/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IMachine {

    boolean isActive();

    boolean isPaused();

    boolean canAcceptAugment(ItemStack augment);

    List<ItemStack> getInstalledAugments();

    List<ItemStack> getInstalledAugments(IAugmentType filter);

    boolean hasAugmentInstalled(IAugmentType augmentType);

}

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.augment;

import com.hrznstudio.titanium.api.IMachine;
import net.minecraft.util.IItemProvider;

public interface IAugment extends IItemProvider {

    IAugmentType getAugmentType();

    float getAugmentRatio();

    boolean canWorkIn(IMachine machine);
}

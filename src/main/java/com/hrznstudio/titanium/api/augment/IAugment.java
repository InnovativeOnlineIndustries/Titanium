/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.augment;

import com.hrznstudio.titanium.api.IMachine;
import net.minecraft.util.IItemProvider;

public interface IAugment extends IItemProvider {

    IAugmentType getAugmentType();

    float getAugmentRatio();

    boolean canWorkIn(IMachine machine);
}

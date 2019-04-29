/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api;

import com.hrznstudio.titanium.api.augment.IAugment;
import com.hrznstudio.titanium.api.augment.IAugmentType;

import java.util.List;

public interface IMachine {
    boolean isActive();

    boolean isPaused();

    boolean canAcceptAugment(IAugment augment);

    List<IAugment> getInstalledAugments();

    List<IAugment> getInstalledAugments(IAugmentType filter);
}
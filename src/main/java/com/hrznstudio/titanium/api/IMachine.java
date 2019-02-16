/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.api;

import com.hrznstudio.titanium.api.augment.IAugment;
import com.hrznstudio.titanium.api.augment.IAugmentType;
import com.hrznstudio.titanium.block.tile.TileMachine;

import java.util.List;

public interface IMachine {
    boolean isActive();

    boolean isPaused();

    boolean canAcceptAugment(IAugment machine);

    List<IAugment> getInstalledAugments();

    List<IAugment> getInstalledAugments(IAugmentType filter);
}
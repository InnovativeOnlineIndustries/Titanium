/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
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

    boolean hasAugmentInstalled(IAugmentType augmentType);

}
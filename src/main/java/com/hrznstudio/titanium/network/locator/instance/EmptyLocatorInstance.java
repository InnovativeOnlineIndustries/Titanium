/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

public class EmptyLocatorInstance extends LocatorInstance {
    public EmptyLocatorInstance() {
        super(LocatorTypes.EMPTY);
    }

    @Nonnull
    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return Optional.empty();
    }
}

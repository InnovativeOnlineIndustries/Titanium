/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class EmptyLocatorInstance extends LocatorInstance {
    public EmptyLocatorInstance() {
        super(LocatorTypes.EMPTY);
    }

    @Override
    public Optional<?> locale(Player playerEntity) {
        return Optional.empty();
    }
}

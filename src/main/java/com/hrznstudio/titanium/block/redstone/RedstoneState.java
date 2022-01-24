/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.redstone.IRedstoneState;

public enum RedstoneState implements IRedstoneState {
    ON(true),
    OFF(false);

    private final boolean isRedstoneOn;

    RedstoneState(boolean isRedstoneOn) {
        this.isRedstoneOn = isRedstoneOn;
    }

    @Override
    public boolean isReceivingRedstone() {
        return isRedstoneOn;
    }
}

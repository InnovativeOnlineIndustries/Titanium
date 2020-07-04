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

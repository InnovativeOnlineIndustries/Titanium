package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.redstone.IRedstoneState;

public enum RedstoneEnviroment implements IRedstoneState {
    ON(true),
    OFF(false);

    private final boolean isRedstoneOn;

    RedstoneEnviroment(boolean isRedstoneOn) {
        this.isRedstoneOn = isRedstoneOn;
    }

    @Override
    public boolean isReceivingRedstone() {
        return isRedstoneOn;
    }
}

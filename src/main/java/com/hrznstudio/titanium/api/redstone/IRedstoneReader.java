package com.hrznstudio.titanium.api.redstone;

import net.minecraft.util.Direction;

public interface IRedstoneReader {

    IRedstoneState getEnvironmentValue(boolean strongPower, Direction direction);

}

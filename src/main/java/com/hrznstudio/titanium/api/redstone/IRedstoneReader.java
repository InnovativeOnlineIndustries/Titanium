package com.hrznstudio.titanium.api.redstone;

import net.minecraft.util.Direction;

public interface IRedstoneReader<T extends IRedstoneState> {

    T getEnvironmentValue(boolean strongPower, Direction direction);

}

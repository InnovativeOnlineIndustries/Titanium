package com.hrznstudio.titanium.api.redstone;

import com.hrznstudio.titanium.util.FacingUtil;

public interface IRedstoneReader<T extends IRedstoneState> {

    T getEnvironmentValue(boolean strongPower, FacingUtil.Sideness sideness);

}

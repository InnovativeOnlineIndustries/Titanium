/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.redstone;

import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRedstoneReader {

    @Nonnull
    IRedstoneState getEnvironmentValue(boolean strongPower, @Nullable Direction direction);

}

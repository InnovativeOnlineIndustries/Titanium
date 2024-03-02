/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block_network;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface INetworkDirectionalConnection {
    boolean canConnect(BlockState state, Direction direction);

}

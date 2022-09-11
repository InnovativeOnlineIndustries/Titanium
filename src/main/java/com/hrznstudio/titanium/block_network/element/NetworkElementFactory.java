/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block_network.element;

import com.hrznstudio.titanium.api.block_network.NetworkElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface NetworkElementFactory {
    NetworkElement createFromNbt(Level level, CompoundTag tag);

}

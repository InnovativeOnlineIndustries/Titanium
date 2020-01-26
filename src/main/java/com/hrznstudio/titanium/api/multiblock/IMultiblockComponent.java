/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.util.math.BlockPos;

public interface IMultiblockComponent {
    boolean isFormed();
    void setFormed(boolean formed);
    BlockPos getPosition();
    BlockPos getMasterPosition();
}

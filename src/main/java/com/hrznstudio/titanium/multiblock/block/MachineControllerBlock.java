/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.multiblock.tile.MachineControllerTile;

public class MachineControllerBlock<T extends MachineControllerTile<T>> extends RotatableBlock<T> {
    public MachineControllerBlock(Properties properties, Class<T> tileClass) {
        super(properties, tileClass);
    }

    @Override
    public IFactory<T> getTileEntityFactory() {
        return null;
    }
}

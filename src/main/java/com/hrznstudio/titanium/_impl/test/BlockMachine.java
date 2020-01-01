/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TileMachineTest;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;
import net.minecraft.block.Blocks;

public class BlockMachine extends BlockRotation<TileMachineTest> {

    public static BlockMachine TEST;

    public BlockMachine() {
        super("machine_test", Properties.from(Blocks.IRON_BLOCK), TileMachineTest.class);
    }

    @Override
    public IFactory<TileMachineTest> getTileEntityFactory() {
        return () -> new TileMachineTest();
    }
}

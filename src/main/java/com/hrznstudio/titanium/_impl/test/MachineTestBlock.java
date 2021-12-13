/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.MachineTestTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MachineTestBlock extends RotatableBlock<MachineTestTile> {

    public static MachineTestBlock TEST;

    public MachineTestBlock() {
        super("machine_test", Properties.copy(Blocks.IRON_BLOCK), MachineTestTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<MachineTestTile> getTileEntityFactory() {
        return MachineTestTile::new;
    }
}

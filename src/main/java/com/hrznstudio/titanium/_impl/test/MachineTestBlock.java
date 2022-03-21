/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.MachineTestTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

public class MachineTestBlock extends RotatableBlock<MachineTestTile> {

    public static Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> TEST;

    public MachineTestBlock() {
        super("machine_test", Properties.copy(Blocks.IRON_BLOCK), MachineTestTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<MachineTestTile> getTileEntityFactory() {
        return MachineTestTile::new;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TwentyFourTestTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class TwentyFourTestBlock extends RotatableBlock<TwentyFourTestTile> {

    public static RegistryObject<Block> TEST;

    public TwentyFourTestBlock() {
        super("twenty_four_test", Properties.of(Material.STONE), TwentyFourTestTile.class);
    }


    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.TWENTY_FOUR_WAY;
    }

    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<TwentyFourTestTile> getTileEntityFactory() {
        return TwentyFourTestTile::new;
    }
}

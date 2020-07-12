/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TestTile;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.loot.LootTable;

import javax.annotation.Nonnull;

@ConfigFile.Child(TitaniumConfig.class)
public class TestBlock extends RotatableBlock<TestTile> {

    @ConfigVal
    public static int DUMB_VALUE = 135;

    public static TestBlock TEST;

    public TestBlock() {
        super(Properties.create(Material.ROCK), TestTile.class);
    }

    @Override
    public IFactory<TestTile> getTileEntityFactory() {
        return TestTile::new;
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TestTile;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

@ConfigFile.Child(TitaniumConfig.class)
public class TestBlock extends RotatableBlock<TestTile> {

    @ConfigVal
    public static int DUMB_VALUE = 135;

    public static Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> TEST;

    public TestBlock() {
        super("test", Properties.copy(Blocks.STONE), TestTile.class);
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

    @Override
    public BlockEntityType.BlockEntitySupplier<TestTile> getTileEntityFactory() {
        return TestTile::new;
    }
}

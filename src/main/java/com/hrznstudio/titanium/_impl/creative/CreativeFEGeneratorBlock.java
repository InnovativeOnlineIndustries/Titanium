/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative;

import com.hrznstudio.titanium._impl.creative.tile.CreativeFEGeneratorTile;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import com.hrznstudio.titanium.module.BlockWithTile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootTable;

public class CreativeFEGeneratorBlock extends BasicTileBlock<CreativeFEGeneratorTile> {

    public static BlockWithTile INSTANCE;

    public CreativeFEGeneratorBlock() {
        super(Block.Properties.ofFullCopy(Blocks.BEDROCK), CreativeFEGeneratorTile.class);
    }


    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<CreativeFEGeneratorTile> getTileEntityFactory() {
        return CreativeFEGeneratorTile::new;
    }
}

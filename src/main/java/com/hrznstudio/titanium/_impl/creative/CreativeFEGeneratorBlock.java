/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium._impl.creative.tile.CreativeFEGeneratorTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;

public class CreativeFEGeneratorBlock extends BasicTileBlock<CreativeFEGeneratorTile> {

    public static CreativeFEGeneratorBlock INSTANCE = (CreativeFEGeneratorBlock) new CreativeFEGeneratorBlock()
        .setRegistryName(Titanium.MODID, "creative_fe_generator");

    public CreativeFEGeneratorBlock() {
        super(Block.Properties.from(Blocks.BEDROCK), CreativeFEGeneratorTile.class);
    }

    @Override
    public IFactory<CreativeFEGeneratorTile> getTileEntityFactory() {
        return CreativeFEGeneratorTile::new;
    }

    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }
}

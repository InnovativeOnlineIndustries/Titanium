/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative;

import com.hrznstudio.titanium._impl.creative.tile.TileCreativeFEGenerator;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.recipe.generator.TitaniumLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockCreativeFEGenerator extends BlockTileBase<TileCreativeFEGenerator> {

    public static BlockCreativeFEGenerator INSTANCE = new BlockCreativeFEGenerator();

    public BlockCreativeFEGenerator() {
        super("creative_fe_generator", Block.Properties.from(Blocks.BEDROCK), TileCreativeFEGenerator.class);
    }

    @Override
    public IFactory<TileCreativeFEGenerator> getTileEntityFactory() {
        return TileCreativeFEGenerator::new;
    }

    @Override
    public void createLootTable(TitaniumLootTableProvider provider) {
        provider.createEmpty(this);
    }
}

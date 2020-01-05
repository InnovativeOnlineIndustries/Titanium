/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.creative;

import com.hrznstudio.titanium._impl.creative.tile.CreativeFEGeneratorTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.recipe.generator.TitaniumLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class CreativeFEGeneratorBlock extends BasicTileBlock<CreativeFEGeneratorTile> {

    public static CreativeFEGeneratorBlock INSTANCE = new CreativeFEGeneratorBlock();

    public CreativeFEGeneratorBlock() {
        super("creative_fe_generator", Block.Properties.from(Blocks.BEDROCK), CreativeFEGeneratorTile.class);
    }

    @Override
    public IFactory<CreativeFEGeneratorTile> getTileEntityFactory() {
        return CreativeFEGeneratorTile::new;
    }

    @Override
    public void createLootTable(TitaniumLootTableProvider provider) {
        provider.createEmpty(this);
    }
}

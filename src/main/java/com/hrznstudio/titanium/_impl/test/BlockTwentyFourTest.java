/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TileTwentyFourTest;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;
import com.hrznstudio.titanium.recipe.generator.TitaniumLootTableProvider;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;

public class BlockTwentyFourTest extends BlockRotation<TileTwentyFourTest> {

    public static BlockTwentyFourTest TEST;

    public BlockTwentyFourTest() {
        super("block_twenty_four_test", Properties.create(Material.ROCK), TileTwentyFourTest.class);
    }

    @Override
    public IFactory<TileTwentyFourTest> getTileEntityFactory() {
        return TileTwentyFourTest::new;
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.TWENTY_FOUR_WAY;
    }

    @Override
    public void createLootTable(TitaniumLootTableProvider provider) {
        provider.createEmpty(this);
    }
}

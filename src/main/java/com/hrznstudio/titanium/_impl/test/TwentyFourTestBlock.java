/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TwentyFourTestTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.datagenerator.loot.TitaniumLootTableProvider;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;

public class TwentyFourTestBlock extends RotatableBlock<TwentyFourTestTile> {

    public static TwentyFourTestBlock TEST;

    public TwentyFourTestBlock() {
        super("block_twenty_four_test", Properties.create(Material.ROCK), TwentyFourTestTile.class);
    }

    @Override
    public IFactory<TwentyFourTestTile> getTileEntityFactory() {
        return TwentyFourTestTile::new;
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

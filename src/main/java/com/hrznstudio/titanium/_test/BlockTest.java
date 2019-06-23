/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;

public class BlockTest extends BlockRotation<TileTest> {

    public static BlockTest TEST;

    public BlockTest() {
        super("block_test", Properties.create(Material.ROCK), TileTest.class);
    }

    @Override
    public IFactory<TileTest> getTileEntityFactory() {
        return TileTest::new;
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.material.Material;

public class BlockTest extends BlockTileBase<TileTest> {

    public static BlockTest TEST;

    public BlockTest() {
        super("block_test", Properties.create(Material.ROCK), TileTest.class);
    }

    @Override
    public IFactory<TileTest> getTileEntityFactory() {
        return TileTest::new;
    }
}

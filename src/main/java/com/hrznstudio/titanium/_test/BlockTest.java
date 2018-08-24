/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.base.api.IFactory;
import com.hrznstudio.titanium.base.block.BlockTileBase;
import net.minecraft.block.material.Material;

public class BlockTest extends BlockTileBase<TileTest> {

    public BlockTest() {
        super("block_test", Material.ROCK, TileTest.class);
    }

    @Override
    public IFactory<TileTest> getTileEntityFactory() {
        return TileTest::new;
    }
}

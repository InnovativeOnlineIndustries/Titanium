/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;
import net.minecraft.block.material.Material;

public class BlockTwentyFourTest extends BlockRotation<TileTwentyFourTest> {

    public static BlockTwentyFourTest TEST;

    public BlockTwentyFourTest() {
        super("block_twenty_four_test", Properties.create(Material.ROCK), TileTwentyFourTest.class);
        setRotationType(RotationType.TWENTY_FOUR_WAY);
    }

    @Override
    public IFactory<TileTwentyFourTest> getTileEntityFactory() {
        return TileTwentyFourTest::new;
    }
}

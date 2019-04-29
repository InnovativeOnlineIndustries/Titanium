/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
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

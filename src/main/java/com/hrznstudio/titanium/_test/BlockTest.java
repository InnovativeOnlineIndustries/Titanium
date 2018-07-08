package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
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

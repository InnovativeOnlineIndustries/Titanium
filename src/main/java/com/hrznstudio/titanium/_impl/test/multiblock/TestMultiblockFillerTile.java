package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.multiblock.tile.MultiblockFillerTile;
import net.minecraftforge.registries.ObjectHolder;

public class TestMultiblockFillerTile extends MultiblockFillerTile<TestMultiblockFillerTile> {

    @ObjectHolder("titanium:test_filler_block")
    public static TestMultiblockFillerBlock fillerBlock;

    public TestMultiblockFillerTile() {
        super(fillerBlock);
    }

}

package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.multiblock.tile.MultiblockControllerTile;
import net.minecraftforge.registries.ObjectHolder;

public class TestMultiblockControllerTile extends MultiblockControllerTile<TestMultiblockControllerTile> {

    public TestMultiblockControllerTile() {
        super(new TestMultiblockTemplate(), TestMultiblockControllerBlock.TEST);
    }
}

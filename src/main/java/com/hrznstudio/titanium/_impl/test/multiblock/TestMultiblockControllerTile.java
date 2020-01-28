/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.multiblock.tile.MultiblockControllerTile;
import net.minecraftforge.registries.ObjectHolder;

public class TestMultiblockControllerTile extends MultiblockControllerTile<TestMultiblockControllerTile> {

    public TestMultiblockControllerTile() {
        super(new TestMultiblockTemplate(), TestMultiblockControllerBlock.TEST);
    }
}

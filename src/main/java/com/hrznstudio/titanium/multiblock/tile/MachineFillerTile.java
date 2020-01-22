/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.tile;

import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.multiblock.block.MachineFillerBlock;
import net.minecraft.block.BlockState;

import javax.annotation.Nonnull;

public class MachineFillerTile extends ActiveTile<MachineFillerTile> implements IMultiblockComponent {
    private BlockState originalState;
    private boolean formed = false;

    public MachineFillerTile(BasicTileBlock<MachineFillerTile> base, BlockState originalState) {
        super(base);
        this.originalState = originalState;
    }

    public BlockState getOriginalState() {
        return originalState;
    }

    @Nonnull
    @Override
    public MachineFillerTile getSelf() {
        return null;
    }

    @Override
    public boolean isFormed() {
        return false;
    }

    public void setFormed(boolean formed) {
        this.formed = formed;
    }
}

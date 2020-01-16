package com.hrznstudio.titanium.multiblock.tile;

import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.multiblock.block.MachineFillerBlock;
import net.minecraft.block.BlockState;

import javax.annotation.Nonnull;

public class MachineFillerTile extends ActiveTile<MachineFillerTile> {
    private BlockState originalState;


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
}

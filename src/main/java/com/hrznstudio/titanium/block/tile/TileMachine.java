package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.block.BlockMachine;
import net.minecraft.util.ITickable;

public class TileMachine extends TilePowered implements ITickable {
    public TileMachine(BlockMachine<?> blockMachine) {
        super(blockMachine);
    }

    @Override
    public void tick() {
        super.tick();
    }
}

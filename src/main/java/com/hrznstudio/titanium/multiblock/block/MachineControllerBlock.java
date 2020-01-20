package com.hrznstudio.titanium.multiblock.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.multiblock.tile.MachineControllerTile;

public class MachineControllerBlock<T extends MachineControllerTile<T>> extends BasicTileBlock<T> {
    public MachineControllerBlock(Properties properties, Class<T> tileClass) {
        super(properties, tileClass);
    }

    @Override
    public IFactory<T> getTileEntityFactory() {
        return null;
    }
}

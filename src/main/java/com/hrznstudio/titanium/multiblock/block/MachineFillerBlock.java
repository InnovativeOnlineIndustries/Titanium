package com.hrznstudio.titanium.multiblock.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.multiblock.tile.MachineFillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;

public class MachineFillerBlock extends BasicTileBlock<MachineFillerTile> {
    private BlockState originalState;
    public static MachineFillerBlock INSTANCE = new MachineFillerBlock();

    public MachineFillerBlock() {
        super(Properties.create(Material.IRON), MachineFillerTile.class);
    }

    public MachineFillerBlock(Properties properties) {
        super(properties, MachineFillerTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return null;
    }

    public BlockState getOriginalState() {
        return originalState;
    }
}

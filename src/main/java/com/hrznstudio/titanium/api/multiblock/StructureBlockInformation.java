package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

public class StructureBlockInformation {
    private BlockState state;
    private VoxelShape shape;

    public StructureBlockInformation(BlockState state, VoxelShape shape) {
        this.state = state;
        this.shape = shape;
    }

    public BlockState getState() {
        return state;
    }

    public VoxelShape getShape() {
        return shape;
    }
}

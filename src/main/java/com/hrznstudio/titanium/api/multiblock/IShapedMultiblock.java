package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IShapedMultiblock {

    /**
     * @return Returns the three-dimensional array of Blockstates[x][y][z] for the Multiblock
     */
    BlockState[][][] getMultiblock();

    /**
     * @return Returns an Array of VoxelShape(s) that the Multiblock VoxelShape is comprised off
     */
    List<VoxelShape> getComponentMultiblockShapes();

    /**
     * @return Returns the final VoxelShape for the Multiblock
     * Used for @Block#getShape && @Block#getRaytraceShape to provide the proper collision and raytracing shapes for the formed Multiblock.
     */
    default VoxelShape getCompiledMultiblockVoxelShape(List<VoxelShape> shapes) {
        VoxelShape finalShape = VoxelShapes.empty();
        for (VoxelShape voxelShape : shapes) {
            finalShape = VoxelShapes.or(finalShape, voxelShape);
        }
        return finalShape;
    }

    /**
     * @param state
     * Used to check whether the given blockstate can be used to trigger the formation of the Multiblock Structure.
     */
    boolean isBlockTrigger(BlockState state);

    /**
     * @param world
     * @param pos
     * @param direction
     * @param playerEntity
     * This is used for the final check for the structure and sets the new structure.
     */
    boolean createStructure(World world, BlockPos pos, Direction direction, PlayerEntity playerEntity);

    /**
     * Used to check if the Multiblock can be rendered in @IShapedMultiblock#renderFormedMultiblock
     * @value True = Can Render the Multiblock
     * @value False = Can't Render the Multiblock
     */
    @OnlyIn(Dist.CLIENT)
    boolean canRenderFormedMultiblock();

    /**
     * Use this function to do the rendering for the Multiblock.
     */
    @OnlyIn(Dist.CLIENT)
    void renderFormedMultiblock();
}

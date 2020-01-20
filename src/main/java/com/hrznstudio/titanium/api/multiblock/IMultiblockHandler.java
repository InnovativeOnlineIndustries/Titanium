package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IMultiblockHandler extends IForgeRegistryEntry<IMultiblockHandler> {
    /**
     * @param state // Trigger BlockState
     * Used to check whether the given blockstate can be used to trigger the formation of the Multiblock Structure.
     */
    boolean isBlockTrigger(BlockState state);

    /**
     * @param world //Server World
     * @param controllerPos //Controller BlockPos
     * @param direction //Direction Controller was Clicked from
     * @param playerEntity //Player who Clicked
     * This is used for the final check for the structure and sets the new structure.
     */
    boolean createStructure(World world, BlockPos controllerPos, Direction direction, PlayerEntity playerEntity);

    /**
     * @param world //Server World
     * @param controllerPos //Controller BlockPos
     * @param direction //Direction Controller was Clicked from
     * @param block //Broken Block
     * @param playerEntity //Player who Clicked
     * This is used for dealing with the breaking and subsequent "resetting" of the blockstates.
     */
    void breakStructure(World world, BlockPos controllerPos, Direction direction, Block block, PlayerEntity playerEntity);

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

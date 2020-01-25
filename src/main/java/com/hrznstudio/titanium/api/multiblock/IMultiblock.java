/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IMultiblock {

    /**
     * @param state Trigger BlockState
     * Used to check whether the given blockstate can be used to trigger the formation of the Multiblock Structure.
     */
    boolean isBlockTrigger(BlockState state);

    /**
     * @param world         World
     * @param controllerPos Controller BlockPos
     * @param direction     Direction Controller was Clicked from
     * @param playerEntity  Player who Clicked
     * This is used for the final check for the structure and sets the new structure.
     */
    boolean createStructure(World world, BlockPos controllerPos, Direction direction, PlayerEntity playerEntity);

    /**
     * @param world     World
     * @param originPos Origin Position
     * @param rotation  Rotation
     * @param mirror    Mirror
     * @param direction Direction
     * This is used for forming the actual structure and replacing blockstates.
     */
    void formStructure(World world, BlockPos originPos, Rotation rotation, Mirror mirror, Direction direction);

    /**
     * @param world     Server World
     * @param originPos Controller BlockPos
     * @param direction Direction Controller was Clicked from
     * @param mirrored  Mirrored Boolean
     * This is used for dealing with the breaking and subsequent "resetting" of the blockstates.
     */
    void breakStructure(World world, BlockPos originPos, Direction direction, boolean mirrored);

    /**
     * Used to check if the Multiblock can be rendered in @IShapedMultiblock#renderFormedMultiblock
     */
    @OnlyIn(Dist.CLIENT)
    boolean canRenderFormedMultiblock();

    /**
     * Use this function to do the rendering for the Multiblock.
     */
    @OnlyIn(Dist.CLIENT)
    void renderFormedMultiblock();
}

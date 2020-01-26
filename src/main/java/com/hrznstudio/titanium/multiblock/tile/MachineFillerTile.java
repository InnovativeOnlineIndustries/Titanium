/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.multiblock.IFormationTool;
import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.api.multiblock.MultiblockTemplate;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.multiblock.TitaniumMultiblockTemplate;
import com.hrznstudio.titanium.multiblock.block.MachineFillerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nonnull;

import static com.hrznstudio.titanium.block.RotatableBlock.FACING_HORIZONTAL;

public class MachineFillerTile extends ActiveTile<MachineFillerTile> implements IMultiblockComponent {

    @Save
    private BlockPos posFromMaster = BlockPos.ZERO;
    @Save
    private BlockPos posInMultiblock = BlockPos.ZERO;
    @Save
    private BlockPos masterPos = BlockPos.ZERO;
    @Save
    private boolean isRedStoneBlock;
    @Save
    private boolean isFormed = false;
    @Save
    private BlockState originalState = getOriginalBlock();

    private MultiblockTemplate multiblockTemplate;

    public MachineFillerTile(BasicTileBlock<MachineFillerTile> base) {
        super(base);
    }

    public boolean isRedStoneBlock() {
        return false;
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

    @Override
    public void setFormed(boolean isFormed) {
        this.isFormed = isFormed;
    }

    public BlockPos getOrigin() {
        return TitaniumMultiblockTemplate.withSettingsAndOffset(pos, BlockPos.ZERO.subtract(posInMultiblock),
                getIsMirrored(), multiblockTemplate.untransformDirection(getFacing()));
    }

    //ToDo:: Add this to RotatableBlock
    public Direction getFacing()
    {
        BlockState state = getBlockState();
        return state.get(FACING_HORIZONTAL);
    }

    public void setFacing(Direction facing)
    {
        BlockState oldState = world.getBlockState(pos);
        BlockState newState = oldState.with(FACING_HORIZONTAL, facing);
        world.setBlockState(pos, newState);
    }

    @Override
    public BlockPos getMasterPosition() {
        return null;
    }

    public boolean getRedStoneSignal(){
        if(isRedStoneBlock()){
            if(world.isBlockPowered(pos)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public BlockState getOriginalBlock() {
        for(Template.BlockInfo block : multiblockTemplate.getStructure()) {}
        return Blocks.AIR.getDefaultState();
    }
}

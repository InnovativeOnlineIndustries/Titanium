/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.api.multiblock.ShapedMultiblockTemplate;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static com.hrznstudio.titanium.block.RotatableBlock.FACING_HORIZONTAL;

public class MultiblockFillerTile<T extends MultiblockFillerTile<T>> extends ActiveTile<T> implements IMultiblockComponent {

    @Save
    public BlockState originalState = this.getBlockState();
    @Save
    private BlockPos masterPos = BlockPos.ZERO;
    @Save
    private boolean isRedStoneBlock;
    @Save
    private boolean isIOBlock;
    @Save
    private boolean isFormed = false;
    private ShapedMultiblockTemplate multiblockTemplate;

    public MultiblockFillerTile(BasicTileBlock<T> base) {
        super(base);
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, facing, hitX, hitY, hitZ) == ActionResultType.PASS) {
            if (isFormed()) {
                BlockPos masterPosition = getMasterPosition();
                TileEntity master = world.getTileEntity(masterPosition);
                if (master instanceof MultiblockControllerTile) {
                    ((MultiblockControllerTile) master).openGui(player);
                }
            }
        }
        return ActionResultType.FAIL;
    }

    public boolean isRedStoneBlock() {
        return false;
    }

    public void setMultiblockTemplate(ShapedMultiblockTemplate multiblockTemplate) {
        this.multiblockTemplate = multiblockTemplate;
    }

    public boolean isIOBlock() {
        return false;
    }

    public BlockState getOriginalState() {
        return originalState;
    }

    @Nonnull
    @Override
    public T getSelf() {
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

    //ToDo:: Add this to RotatableBlock
    public Direction getFacing() {
        BlockState state = getBlockState();
        return state.get(FACING_HORIZONTAL);
    }

    public void setFacing(Direction facing) {
        BlockState oldState = world.getBlockState(pos);
        BlockState newState = oldState.with(FACING_HORIZONTAL, facing);
        world.setBlockState(pos, newState);
    }

    @Override
    public BlockPos getMasterPosition() {
        return masterPos;
    }

    public void setMasterPosition(BlockPos master) {
        masterPos = master;
    }

    public boolean getRedStoneSignal() {
        if (isRedStoneBlock()) {
            if (world.isBlockPowered(pos)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}

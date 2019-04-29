/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.block.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public abstract class BlockRotation<T extends TileBase> extends BlockTileBase<T> {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", EnumFacing.values());
    public static final DirectionProperty SUB_FACING = DirectionProperty.create("subfacing", EnumFacing.Plane.HORIZONTAL);
    private RotationType rotationType = RotationType.NONE;

    public BlockRotation(String name, Properties properties, Class<T> tileClass) {
        super(name, properties, tileClass);
    }

    public RotationType getRotationType() {
        return rotationType;
    }

    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return getRotationType().getHandler().getStateForPlacement(this, p_196258_1_);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> p_206840_1_) {
        super.fillStateContainer(p_206840_1_);
        p_206840_1_.add(FACING, SUB_FACING);
    }

    public enum RotationType {
        NONE((block, context) -> block.getDefaultState()),
        FOUR_WAY(((block, context) -> block.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()))),
        SIX_WAY((block, context) -> block.getDefaultState().with(FACING, context.getNearestLookingDirection())),
        TWENTY_FOUR_WAY((block, context) -> {
            //TODO: Sub facing
            return block.getDefaultState().with(FACING, context.getNearestLookingDirection());
        });

        private final RotationHandler handler;

        RotationType(RotationHandler handler) {
            this.handler = handler;
        }

        public RotationHandler getHandler() {
            return handler;
        }
    }
}

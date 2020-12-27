/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.block.tile.BasicTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RotatableBlock<T extends BasicTile<T>> extends BasicTileBlock<T> {
    public static final DirectionProperty FACING_ALL = DirectionProperty.create("facing", Direction.values());
    public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("subfacing", Direction.Plane.HORIZONTAL);

    public RotatableBlock(Properties properties, Class<T> tileClass) {
        super(properties, tileClass);
    }

    @Nonnull
    public RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return getRotationType().getHandler().getStateForPlacement(this, p_196258_1_);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        super.fillStateContainer(p_206840_1_);
        if (this.getRotationType().getProperties() != null) p_206840_1_.add(this.getRotationType().getProperties());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (getRotationType().getProperties().length > 0){
            return state.with(getRotationType().getProperties()[0], rot.rotate(state.get(getRotationType().getProperties()[0])));
        }
        return super.rotate(state, rot);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (getRotationType().getProperties().length > 0){
            return state.rotate(mirrorIn.toRotation(state.get(getRotationType().getProperties()[0])));
        }
        return super.mirror(state, mirrorIn);
    }

    public enum RotationType {
        NONE((block, context) -> block.getDefaultState()),
        FOUR_WAY(((block, context) -> block.getDefaultState().with(FACING_HORIZONTAL, context.getPlacementHorizontalFacing().getOpposite())), FACING_HORIZONTAL),
        SIX_WAY((block, context) -> block.getDefaultState().with(FACING_ALL, context.getNearestLookingDirection().getOpposite()), FACING_ALL),
        TWENTY_FOUR_WAY((block, context) -> {
            //TODO: Sub facing
            return block.getDefaultState().with(FACING_ALL, context.getNearestLookingDirection());
        }, FACING_ALL, FACING_HORIZONTAL);

        private final RotationHandler handler;
        private final DirectionProperty[] properties;

        RotationType(RotationHandler handler, DirectionProperty... properties) {
            this.handler = handler;
            this.properties = properties;
        }

        public RotationHandler getHandler() {
            return handler;
        }

        public DirectionProperty[] getProperties() {
            return properties;
        }
    }
}

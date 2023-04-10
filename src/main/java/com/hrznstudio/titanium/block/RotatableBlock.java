/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.block.tile.BasicTile;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RotatableBlock<T extends BasicTile<T>> extends BasicTileBlock<T> {
    public static final DirectionProperty FACING_ALL = DirectionProperty.create("facing", Direction.values());
    public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("subfacing", Direction.Plane.HORIZONTAL);

    public RotatableBlock(String name, Properties properties, Class<T> tileClass) {
        super(name, properties, tileClass);
    }

    @Nonnull
    public RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        return getRotationType().getHandler().getStateForPlacement(this, p_196258_1_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        if (this.getRotationType().getProperties() != null) p_206840_1_.add(this.getRotationType().getProperties());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (getRotationType().getProperties().length > 0){
            return state.setValue(getRotationType().getProperties()[0], rot.rotate(state.getValue(getRotationType().getProperties()[0])));
        }
        return super.rotate(state, rot);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (getRotationType().getProperties().length > 0){
            return state.rotate(mirrorIn.getRotation(state.getValue(getRotationType().getProperties()[0])));
        }
        return super.mirror(state, mirrorIn);
    }

    public enum RotationType {
        NONE((block, context) -> block.defaultBlockState()),
        FOUR_WAY(((block, context) -> block.defaultBlockState().setValue(FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite())), FACING_HORIZONTAL),
        SIX_WAY((block, context) -> block.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection().getOpposite()), FACING_ALL),
        TWENTY_FOUR_WAY((block, context) -> {
            //TODO: Sub facing
            return block.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection());
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

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TitaniumFluid extends FlowingFluid {
        private final TitaniumFluidInstance titaniumFluidInstance;

    public TitaniumFluid(TitaniumFluidInstance titaniumFluidInstance) {
        this.titaniumFluidInstance = titaniumFluidInstance;
    }

    @Override
    @Nonnull
    public Fluid getFlowing() {
        return titaniumFluidInstance.getFlowingFluid().get();
    }

    @Override
    @Nonnull
    public Fluid getSource() {
        return titaniumFluidInstance.getSourceFluid().get();
    }

    @Override
    public boolean canConvertToSource(FluidState state, Level level, BlockPos pos) {
        return false;
    }

    @Override
    protected boolean canConvertToSource(Level p_256009_) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        // copied from the WaterFluid implementation
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity != null) Block.dropResources(state, worldIn, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(@Nonnull LevelReader world) {
        return 4;
    }

    @Override
    protected int getDropOff(@Nonnull LevelReader world) {
        return 1;
    }

    @Override
    @Nonnull
    public Item getBucket() {
        return titaniumFluidInstance.getBucketFluid().get();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean canBeReplacedWith(FluidState p_215665_1_, BlockGetter p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
        return p_215665_5_ == Direction.DOWN && !p_215665_4_.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(@Nonnull LevelReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 1;
    }

    @Override
    @Nonnull
    protected BlockState createLegacyBlock(@Nonnull FluidState state) {
        return titaniumFluidInstance.getBlockFluid().get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSource(@Nonnull FluidState state) {
        return false;
    }

    @Override
    public int getAmount(@Nonnull FluidState p_207192_1_) {
        return 0;
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == titaniumFluidInstance.getFlowingFluid().get() || fluidIn == titaniumFluidInstance.getSourceFluid().get();
    }

    @Override
    public FluidType getFluidType() {
        return titaniumFluidInstance.getFluidType().get();
    }

    public static class Flowing extends TitaniumFluid {
        {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        public Flowing(TitaniumFluidInstance instance) {
            super(instance);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(@Nonnull FluidState p_207192_1_) {
            return p_207192_1_.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@Nonnull FluidState state) {
            return false;
        }
    }

    public static class Source extends TitaniumFluid {

        public Source(TitaniumFluidInstance instance) {
            super(instance);
        }

        @Override
        public int getAmount(@Nonnull FluidState p_207192_1_) {
            return 8;
        }

        @Override
        public boolean isSource(@Nonnull FluidState state) {
            return true;
        }
    }
}

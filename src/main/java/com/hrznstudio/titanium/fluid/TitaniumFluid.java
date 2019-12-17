/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public class TitaniumFluid extends FlowingFluid {

    private final FluidAttributes.Builder fluidAttributes;
    private Fluid flowingFluid;
    private FlowingFluid sourceFluid;
    private Item bucketFluid;
    private Block blockFluid;

    public TitaniumFluid(FluidAttributes.Builder fluidAttributes) {
        this.fluidAttributes = fluidAttributes;
    }

    @Override
    public Fluid getFlowingFluid() {
        return flowingFluid;
    }

    public TitaniumFluid setFlowingFluid(Fluid flowingFluid) {
        this.flowingFluid = flowingFluid;
        return this;
    }

    @Override
    public Fluid getStillFluid() {
        return sourceFluid;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        // copied from the WaterFluid implementation
        TileEntity tileentity = state.getBlock().hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn.getWorld(), pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public Item getFilledBucket() {
        return bucketFluid;
    }

    @Override
    protected boolean canDisplace(IFluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
        return p_215665_5_ == Direction.DOWN && !p_215665_4_.isIn(FluidTags.WATER);
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 1;
    }

    @Override
    protected BlockState getBlockState(IFluidState state) {
        return blockFluid.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isSource(IFluidState state) {
        return false;
    }

    @Override
    public int getLevel(IFluidState p_207192_1_) {
        return 0;
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == sourceFluid || fluidIn == flowingFluid;
    }

    public TitaniumFluid setSourceFluid(FlowingFluid sourceFluid) {
        this.sourceFluid = sourceFluid;
        return this;
    }

    public TitaniumFluid setBucketFluid(Item bucketFluid) {
        this.bucketFluid = bucketFluid;
        return this;
    }

    public TitaniumFluid setBlockFluid(Block blockFluid) {
        this.blockFluid = blockFluid;
        return this;
    }

    @Override
    protected FluidAttributes createAttributes() {
        return fluidAttributes.build(this);
    }

    public static class Flowing extends TitaniumFluid {
        {
            setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
        }

        public Flowing(FluidAttributes.Builder fluidAttributes) {
            super(fluidAttributes);
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public int getLevel(IFluidState p_207192_1_) {
            return p_207192_1_.get(LEVEL_1_8);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    public static class Source extends TitaniumFluid {

        public Source(FluidAttributes.Builder fluidAttributes) {
            super(fluidAttributes);
        }

        @Override
        public int getLevel(IFluidState p_207192_1_) {
            return 8;
        }

        @Override
        public boolean isSource(IFluidState state) {
            return true;
        }
    }
}

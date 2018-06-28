/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BlockTileBase<T extends TileBase> extends BlockBase {
    private final Class<T> tileClass;

    public BlockTileBase(String name, Material materialIn, Class<T> tileClass) {
        super(name, materialIn);
        this.tileClass = tileClass;
    }

    public abstract IFactory<T> getTileEntityFactory();

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        getTile(worldIn, pos).ifPresent(tile -> tile.onNeighborChanged(blockIn, fromPos));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return getTile(worldIn, pos).map(tile -> tile.onActivated(playerIn, hand, facing, hitX, hitY, hitZ)).orElseGet(() -> super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ));
    }

    @Nullable
    @Override
    public T createTileEntity(World world, IBlockState state) {
        return getTileEntityFactory().create();
    }

    public Optional<T> getTile(IBlockAccess access, BlockPos pos) {
        TileEntity tile = access.getTileEntity(pos);
        if (tile != null && tileClass.isInstance(tile))
            return Optional.of(tileClass.cast(tile));
        return Optional.empty();
    }
}
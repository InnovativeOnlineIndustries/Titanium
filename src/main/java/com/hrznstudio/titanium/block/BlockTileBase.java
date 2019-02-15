/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class BlockTileBase<T extends TileBase> extends BlockBase implements ITileEntityProvider {
    private final Class<T> tileClass;
    private TileEntityType tileEntityType;

    public BlockTileBase(String name, Properties properties, Class<T> tileClass) {
        super(name, properties);
        this.tileClass = tileClass;
    }

    public void registerTile(IForgeRegistry<TileEntityType<?>> registry) {
        tileEntityType=TileEntityType.register(getRegistryName().toString(), TileEntityType.Builder.create(getTileEntityFactory()::create));
        tileEntityType.setRegistryName(this.getRegistryName());
        registry.register(tileEntityType);
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
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return getTile(worldIn, pos).map(tile -> tile.onActivated(player, hand, side, hitX, hitY, hitZ)).orElseGet(() -> super.onBlockActivated(state, worldIn, pos, player, hand, side, hitX, hitY, hitZ));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return getTileEntityFactory().create();
    }

    public Optional<T> getTile(IWorldReader access, BlockPos pos) {
        return TileUtil.getTileEntity(access, pos, tileClass);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader iBlockReader) {
        return getTileEntityFactory().create();
    }

    public TileEntityType getTileEntityType() {
        return tileEntityType;
    }
}
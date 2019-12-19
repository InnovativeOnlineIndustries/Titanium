/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.module.api.RegistryManager;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public abstract class BasicTileBlock<T extends BasicTileBlock<T, U>, U extends BasicTile<T, U>> extends BasicBlock {
    private final Class<U> tileClass;
    private TileEntityType<U> tileEntityType;

    public BasicTileBlock(Properties properties, Class<U> tileClass) {
        super(properties);
        this.tileClass = tileClass;
    }

    public BasicTileBlock(String name, Properties properties, Class<U> tileClass) {
        super(name, properties);
        this.tileClass = tileClass;
    }

    @Override
    public void addAlternatives(RegistryManager registry) {
        super.addAlternatives(registry);
        NBTManager.getInstance().scanTileClassForAnnotations(tileClass);
        tileEntityType = TileEntityType.Builder.create(getTileEntityFactory()::create, this).build(null);
        tileEntityType.setRegistryName(this.getRegistryName());
        registry.content(TileEntityType.class, tileEntityType);
    }

    public abstract IFactory<U> getTileEntityFactory();

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
        getTile(worldIn, pos)
                .ifPresent(tile -> tile.onNeighborChanged(blockIn, fromPos));
    }

    //On Activated
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        return getTile(world, pos)
                .map(tile -> tile.onActivated(player, hand, ray.getFace(), ray.getHitVec().x, ray.getHitVec().y, ray.getHitVec().z))
                .orElseGet(() -> super.func_225533_a_(state, world, pos, player, hand, ray));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return getTileEntityFactory().create();
    }

    public Optional<U> getTile(IBlockReader access, BlockPos pos) {
        return TileUtil.getTileEntity(access, pos, tileClass);
    }

    public TileEntityType<U> getTileEntityType() {
        return tileEntityType;
    }
}
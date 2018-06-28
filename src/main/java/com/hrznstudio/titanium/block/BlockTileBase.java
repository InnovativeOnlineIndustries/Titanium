/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BlockTileBase<T extends TileEntity> extends BlockBase {
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
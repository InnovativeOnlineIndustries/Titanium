/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.block.tile.ITickableBlockEntity;
import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class BasicTileBlock<T extends BasicTile<T>> extends BasicBlock implements EntityBlock {

    private final Class<T> tileClass;

    public BasicTileBlock(String name, Properties properties, Class<T> tileClass) {
        super(name, properties);
        this.tileClass = tileClass;
        NBTManager.getInstance().scanTileClassForAnnotations(tileClass);
    }

    public abstract BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory();

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
        getTile(worldIn, pos).ifPresent(tile -> tile.onNeighborChanged(blockIn, fromPos));
    }


    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return getTile(worldIn, pos)
            .map(tile -> tile.onActivated(player, hand, ray.getDirection(), ray.getLocation().x, ray.getLocation().y, ray.getLocation().z))
            .orElseGet(() -> super.use(state, worldIn, pos, player, hand, ray));
    }


    public Optional<T> getTile(BlockGetter access, BlockPos pos) {
        return TileUtil.getTileEntity(access, pos, tileClass);
    }

    public Class<T> getTileClass() {
        return tileClass;
    }

    @Nullable
    @Override
    public <R extends BlockEntity> BlockEntityTicker<R> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<R> p_153214_) {
        return (level, pos, state, blockEntity) -> {
            if (blockEntity instanceof ITickableBlockEntity) {
                if (level.isClientSide()) {
                    ((ITickableBlockEntity) blockEntity).clientTick(level, pos, state, blockEntity);
                } else {
                    ((ITickableBlockEntity) blockEntity).serverTick(level, pos, state, blockEntity);
                }
            }
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel p_153210_, T p_153211_) {
        return EntityBlock.super.getListener(p_153210_, p_153211_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return getTileEntityFactory().create(p_153215_, p_153216_);
    }
}

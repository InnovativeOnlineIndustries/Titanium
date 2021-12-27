/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.network.messages.TileFieldNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BasicTile<T extends BasicTile<T>> extends BlockEntity {

    private final BasicTileBlock<T> basicTileBlock;

    public BasicTile(BasicTileBlock<T> base, BlockPos pos, BlockState state) {
        super(base.getTileEntityType(), pos, state);
        this.basicTileBlock = base;
        if (isClient()) {
            initClient();
        }
    }

    @ParametersAreNonnullByDefault
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        return InteractionResult.PASS;
    }

    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    @OnlyIn(Dist.CLIENT)
    public void initClient() {

    }

    // BlockEntity.Read
    @Override
    public void load(CompoundTag compound) {
        NBTManager.getInstance().readTileEntity(this, compound);
        super.load(compound);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        NBTManager.getInstance().writeTileEntity(this, compoundTag);
    }

    public void markForUpdate() {
        this.level.sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
        setChanged();
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        saveAdditional(compoundTag);
        return compoundTag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> tag);
    }

    public void updateNeigh() {
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(worldPosition), this.level.getBlockState(worldPosition), 3);
    }

    public void syncObject(Object object){
        if (isServer()){
            CompoundTag nbt = NBTManager.getInstance().writeTileEntityObject(this, object, new CompoundTag());
            Titanium.NETWORK.sendToNearby(this.level, this.worldPosition, 64, new TileFieldNetworkMessage(this.worldPosition, nbt));
        }
    }

    public void handleSyncObject(CompoundTag nbt){
        NBTManager.getInstance().readTileEntity(this, nbt);
    }

    public boolean isClient() {
        return this.level.isClientSide;
    }

    public boolean isServer() {
        return !isClient();
    }

    public BasicTileBlock<T> getBasicTileBlock() {
        return basicTileBlock;
    }
}

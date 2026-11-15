/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BasicTile<T extends BasicTile<T>> extends BlockEntity implements IScreenInfoProvider {

    private final BasicTileBlock<T> basicTileBlock;

    public BasicTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.basicTileBlock = base;
    }

    @ParametersAreNonnullByDefault
    public ItemInteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    @Override
    public void setLevel(Level p_155231_) {
        super.setLevel(p_155231_);
        if (isClient()) {
            initClient();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void initClient() {

    }

    // BlockEntity.Read
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        NBTManager.getInstance().readTileEntity(this, provider, compound);
        super.loadAdditional(compound, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        NBTManager.getInstance().writeTileEntity(this, compoundTag);
    }

    public void markForUpdate() {
        this.level.sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
        setChanged();
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        saveAdditional(compoundTag, provider);
        return compoundTag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        loadAdditional(pkt.getTag(), provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (be, reg) -> {
            CompoundTag tag = new CompoundTag();
            saveAdditional(tag, reg);
            return tag;
        });
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
        NBTManager.getInstance().readTileEntity(this, level.registryAccess(), nbt);
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

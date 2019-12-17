/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class TileBase extends TileEntity {

    BlockTileBase blockTileBase;

    public TileBase(BlockTileBase base) {
        super(base.getTileEntityType());
        this.blockTileBase = base;
    }

    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        return ActionResultType.PASS;
    }

    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    @Override
    public void read(CompoundNBT compound) {
        NBTManager.getInstance().readTileEntity(this, compound);
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return NBTManager.getInstance().writeTileEntity(this, super.write(compound));
    }

    public void markForUpdate() {
        this.world.notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        markDirty();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(getPos(), 1, tag);
    }

    public void updateNeigh() {
        this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockState().getBlock());
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
    }

    public boolean isClient() {
        return this.world.isRemote;
    }

    public boolean isServer() {
        return !isClient();
    }

    public BlockTileBase getBlockTileBase() {
        return blockTileBase;
    }
}
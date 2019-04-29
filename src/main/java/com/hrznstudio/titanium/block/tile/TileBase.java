/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TileBase extends TileEntity {

    public TileBase(BlockTileBase base) {
        super(base.getTileEntityType());
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    @Override
    public void read(NBTTagCompound compound) {
        NBTManager.getInstance().readTileEntity(this, compound);
        super.read(compound);
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        return NBTManager.getInstance().writeTileEntity(this, super.write(compound));
    }

    public void markForUpdate() {
        this.world.notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        markDirty();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return write(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        write(tag);
        return new SPacketUpdateTileEntity(getPos(), 1, tag);
    }

    public void updateNeigh() {
        this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockState().getBlock());
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
    }
}
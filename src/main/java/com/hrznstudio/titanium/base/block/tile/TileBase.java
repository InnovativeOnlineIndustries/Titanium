/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.block.tile;

import com.hrznstudio.titanium.base.Titanium;
import com.hrznstudio.titanium.base.api.IFactory;
import com.hrznstudio.titanium.base.api.client.IGuiAddon;
import com.hrznstudio.titanium.base.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.base.block.tile.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.base.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.base.inventory.MultiInventoryHandler;
import com.hrznstudio.titanium.base.inventory.PosInvHandler;
import com.hrznstudio.titanium.base.nbthandler.NBTManager;
import com.hrznstudio.titanium.cassandra.client.gui.asset.IAssetProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileBase extends TileEntity implements IGuiAddonProvider, ITickable {

    private MultiInventoryHandler multiInventoryHandler;
    private MultiProgressBarHandler multiProgressBarHandler;

    private List<IFactory<? extends IGuiAddon>> guiAddons;

    public TileBase() {
        this.guiAddons = new ArrayList<>();
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(EntityPlayer player) {
        Titanium.openGui(this, player);
    }

    /*
        Capability Handling
     */
    public void addInventory(PosInvHandler handler) {
        if (multiInventoryHandler == null) multiInventoryHandler = new MultiInventoryHandler();
        multiInventoryHandler.addInventory(handler.setTile(this));
    }

    public void addProgressBar(PosProgressBar posProgressBar) {
        if (multiProgressBarHandler == null) multiProgressBarHandler = new MultiProgressBarHandler();
        multiProgressBarHandler.addBar(posProgressBar.setTile(this));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && multiInventoryHandler != null && multiInventoryHandler.getCapabilityForSide(facing).getSlots() > 0 || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(multiInventoryHandler.getCapabilityForSide(facing));
        return super.getCapability(capability, facing);
    }

    public MultiInventoryHandler getMultiInventoryHandler() {
        return multiInventoryHandler;
    }

    /*
        Client
     */

    public void addGuiAddonFactory(IFactory<? extends IGuiAddon> factory) {
        this.guiAddons.add(factory);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>(guiAddons);
        if (multiInventoryHandler != null) addons.addAll(multiInventoryHandler.getGuiAddons());
        if (multiProgressBarHandler != null) addons.addAll(multiProgressBarHandler.getGuiAddons());
        return addons;
    }

    /*
        NBT Syncing
     */

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NBTManager.getInstance().readTileEntity(this, compound);
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return NBTManager.getInstance().writeTileEntity(this, super.writeToNBT(compound));
    }

    public void markForUpdate() {
        this.world.notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        markDirty();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 1, tag);
    }

    public IAssetProvider getAssetProvider() {
        return IAssetProvider.DEFAULT_PROVIDER;
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (multiProgressBarHandler != null) multiProgressBarHandler.update();
        }
    }
}

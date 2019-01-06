/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.inventory.MultiInventoryHandler;
import com.hrznstudio.titanium.inventory.PosInvHandler;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.NonNullSupplier;
import net.minecraftforge.common.capabilities.OptionalCapabilityInstance;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileBase extends TileEntity implements IGuiAddonProvider, ITickable {

    private MultiInventoryHandler multiInventoryHandler;
    private MultiProgressBarHandler multiProgressBarHandler;

    private List<IFactory<? extends IGuiAddon>> guiAddons;

    public TileBase(BlockTileBase base) {
        super(base.getTileEntityType());
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

    @Nonnull
    @Override
    public <T> OptionalCapabilityInstance<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        System.out.println(cap);
        /*
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(multiInventoryHandler.getCapabilityForSide(facing));
        return super.getCapability(capability, facing);*/
        if(cap==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return OptionalCapabilityInstance.of(new NonNullSupplier<T>() {
                @Nonnull
                @Override
                public T get() {
                    return (T) multiInventoryHandler.getCapabilityForSide(side);
                }
            });
        }
        return OptionalCapabilityInstance.empty();
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

    public IAssetProvider getAssetProvider() {
        return IAssetProvider.DEFAULT_PROVIDER;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (multiProgressBarHandler != null) multiProgressBarHandler.update();
        }
    }
}
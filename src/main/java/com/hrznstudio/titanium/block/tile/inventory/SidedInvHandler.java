/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.block.tile.sideness.SidedHandlerManager;
import com.hrznstudio.titanium.client.gui.addon.FacingHandlerGuiAddon;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SidedInvHandler extends PosInvHandler implements IFacingHandler {

    private int color;
    private HashMap<FacingUtil.Sideness, FaceMode> facingModes;
    private HashMap<FacingUtil.Sideness, Integer> slotCache;
    private int position;

    public SidedInvHandler(String name, int xPos, int yPos, int size, int position) {
        super(name, xPos, yPos, size);
        this.color = EnumDyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        this.slotCache = new HashMap<>();
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            this.facingModes.put(value, FaceMode.ENABLED);
        }
        this.position = position;
    }

    @Override
    public HashMap<FacingUtil.Sideness, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public SidedInvHandler setColor(EnumDyeColor color) {
        this.color = color.getFireworkColor();
        return this;
    }

    public SidedInvHandler setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public Rectangle getRectangle() {
        int renderingOffset = 1;
        return new Rectangle(this.getXPos() - renderingOffset - 3, this.getYPos() - renderingOffset - 3, 18 * this.getXSize() + renderingOffset * 2 + 3, 18 * this.getYSize() + renderingOffset * 2 + 3);
    }

    @Override
    public boolean work(World world, BlockPos pos, EnumFacing blockFacing, int workAmount) {
        for (FacingUtil.Sideness sideness : facingModes.keySet()) {
            if (facingModes.get(sideness).equals(FaceMode.PUSH)) {
                EnumFacing real = FacingUtil.getFacingFromSide(blockFacing, sideness);
                TileEntity entity = world.getTileEntity(pos.offset(real));
                AtomicBoolean hasWorked = new AtomicBoolean(false);
                if (entity != null) {
                    entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, real.getOpposite()).ifPresent(iItemHandler -> {
                        hasWorked.set(transfer(sideness, this, iItemHandler, workAmount));
                    });
                    if (hasWorked.get()) return true;
                }
            }
        }
        for (FacingUtil.Sideness sideness : facingModes.keySet()) {
            if (facingModes.get(sideness).equals(FaceMode.PULL)) {
                EnumFacing real = FacingUtil.getFacingFromSide(blockFacing, sideness);
                TileEntity entity = world.getTileEntity(pos.offset(real));
                AtomicBoolean hasWorked = new AtomicBoolean(false);
                if (entity != null) {
                    entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, real.getOpposite()).ifPresent(iItemHandler -> {
                        hasWorked.set(transfer(sideness, iItemHandler, this, workAmount));
                    });
                    if (hasWorked.get()) return true;
                }
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        NBTTagCompound compound = new NBTTagCompound();
        for (FacingUtil.Sideness facing : facingModes.keySet()) {
            compound.putString(facing.name(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("FacingModes")) {
            NBTTagCompound compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(FacingUtil.Sideness.valueOf(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = super.getGuiAddons();
        addons.add(() -> new FacingHandlerGuiAddon(SidedHandlerManager.ofRight(8, 84, position, AssetTypes.BUTTON_SIDENESS_MANAGER, 4), this));
        return addons;
    }

    private int getNextSlot(IItemHandler handler, int currentSlot) {
        for (int i = currentSlot; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) return i;
        }
        return 0;
    }

    private boolean transfer(FacingUtil.Sideness sideness, IItemHandler from, IItemHandler to, int workAmount) {
        int slot = slotCache.getOrDefault(sideness, getNextSlot(from, 0));
        if (from.getSlots() < slot) slot = 0;
        ItemStack extracted = from.extractItem(slot, workAmount, true);
        if (!extracted.isEmpty()) {
            ItemStack returned = ItemHandlerHelper.insertItemStacked(to, extracted, false);
            return !from.extractItem(slot, extracted.getCount() - returned.getCount(), false).isEmpty();
        } else {
            slotCache.put(sideness, getNextSlot(from, slot));
        }
        return false;
    }
}

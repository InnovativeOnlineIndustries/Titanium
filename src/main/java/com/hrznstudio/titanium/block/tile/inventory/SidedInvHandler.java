/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.block.tile.sideness.SidedHandlerManager;
import com.hrznstudio.titanium.client.gui.addon.FacingHandlerGuiAddon;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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
    private int facingHandlerX = 8;
    private int facingHandlerY = 84;
    private HashMap<FacingUtil.Sideness, FaceMode> facingModes;
    private HashMap<FacingUtil.Sideness, Integer> slotCache;
    private int position;
    private boolean colorGuiEnabled;
    private boolean hasFacingAddon;

    public SidedInvHandler(String name, int xPos, int yPos, int size, int position) {
        super(name, xPos, yPos, size);
        this.color = DyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        this.slotCache = new HashMap<>();
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            this.facingModes.put(value, FaceMode.ENABLED);
        }
        this.position = position;
        this.colorGuiEnabled = true;
        this.hasFacingAddon = true;
    }

    public SidedInvHandler disableFacingAddon() {
        this.hasFacingAddon = false;
        return this;
    }

    @Override
    public HashMap<FacingUtil.Sideness, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public SidedInvHandler setColor(int color) {
        this.color = color;
        return this;
    }

    public SidedInvHandler setColor(DyeColor color) {
        this.color = color.getFireworkColor();
        return this;
    }

    public boolean isColorGuiEnabled() {
        return colorGuiEnabled;
    }

    public SidedInvHandler setColorGuiEnabled(boolean colorGuiEnabled) {
        this.colorGuiEnabled = colorGuiEnabled;
        return this;
    }

    @Override
    public Rectangle getRectangle(IAsset asset) {
        int renderingOffset = 1;
        return new Rectangle(this.getXPos() - renderingOffset - 3, this.getYPos() - renderingOffset - 3, (int) asset.getArea().getWidth() * this.getXSize() + renderingOffset * 2 + 3, (int) asset.getArea().getHeight() * this.getYSize() + renderingOffset * 2 + 3);
    }

    @Override
    public int getFacingHandlerX() {
        return this.facingHandlerX;
    }

    @Override
    public int getFacingHandlerY() {
        return this.facingHandlerY;
    }

    @Override
    public boolean work(World world, BlockPos pos, Direction blockFacing, int workAmount) {
        for (FacingUtil.Sideness sideness : facingModes.keySet()) {
            if (facingModes.get(sideness).equals(FaceMode.PUSH)) {
                Direction real = FacingUtil.getFacingFromSide(blockFacing, sideness);
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
                Direction real = FacingUtil.getFacingFromSide(blockFacing, sideness);
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
    public SidedInvHandler setFacingHandlerPos(int x, int y) {
        this.facingHandlerX = x;
        this.facingHandlerY = y;
        return this;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        CompoundNBT compound = new CompoundNBT();
        for (FacingUtil.Sideness facing : facingModes.keySet()) {
            compound.putString(facing.name(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("FacingModes")) {
            CompoundNBT compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(FacingUtil.Sideness.valueOf(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = super.getGuiAddons();
        if (hasFacingAddon)
            addons.add(() -> new FacingHandlerGuiAddon(SidedHandlerManager.ofRight(getFacingHandlerX(), getFacingHandlerY(), position, AssetTypes.BUTTON_SIDENESS_MANAGER, 4), this, AssetTypes.SLOT));
        return addons;
    }

    private int getNextSlot(IItemHandler handler, int currentSlot) {
        for (int i = currentSlot; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) return i;
        }
        return 0;
    }

    private boolean transfer(FacingUtil.Sideness sideness, IItemHandler from, IItemHandler to, int workAmount) {
        if (from.getSlots() <= 0) return false;
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

/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.container;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.inventory.PosInvHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import java.awt.*;

public class ContainerTileBase<T extends TileBase> extends Container {

    private T tile;
    private InventoryPlayer player;
    private boolean hasPlayerInventory;
    private int totalSlots;

    public ContainerTileBase(T tile, InventoryPlayer player) {
        this.tile = tile;
        this.player = player;
        this.totalSlots = 0;
        if (tile.getMultiInventoryHandler() != null) {
            for (PosInvHandler handler : tile.getMultiInventoryHandler().getInventoryHandlers()) {
                int i = 0;
                for (int y = 0; y < handler.getYSize(); ++y) {
                    for (int x = 0; x < handler.getXSize(); ++x) {
                        addSlot(new SlotItemHandler(handler, i, handler.getXPos() + x * 18, handler.getYPos() + y * 18));
                        ++i;
                    }
                }
            }
        }
        Point hotbarPos = IAssetProvider.getAsset(tile.getAssetProvider(), AssetTypes.BACKGROUND).getHotbarPosition();
        addPlayerChestInventory();
        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(player, k, hotbarPos.x + k * 18, hotbarPos.y));
        }
    }

    public void addPlayerChestInventory() {
        Point invPos = IAssetProvider.getAsset(tile.getAssetProvider(), AssetTypes.BACKGROUND).getInventoryPosition();
        if (hasPlayerInventory) return;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player, j + i * 9 + 9, invPos.x + j * 18, invPos.y + i * 18));
            }
        }
        hasPlayerInventory = true;
    }

    public void removeChestInventory() {
        this.inventorySlots.removeIf(slot -> slot.getSlotIndex() >= 9 && slot.getSlotIndex() < 9 + 3 * 9);
        hasPlayerInventory = false;
    }

    public void updateSlotPosition() {
        if (tile.getMultiInventoryHandler() != null) {
            for (PosInvHandler handler : tile.getMultiInventoryHandler().getInventoryHandlers()) {
                int i = 0;
                for (int y = 0; y < handler.getYSize(); ++y) {
                    for (int x = 0; x < handler.getXSize(); ++x) {
                        for (Slot inventorySlot : this.inventorySlots) {
                            if (!(inventorySlot instanceof SlotItemHandler)) continue;
                            if (((SlotItemHandler) inventorySlot).getItemHandler().equals(handler) && i == inventorySlot.getSlotIndex()) {
                                inventorySlot.xPos = handler.getXPos() + x * 18;
                                inventorySlot.yPos = handler.getYPos() + y * 18;
                                break;
                            }
                        }
                        ++i;
                    }
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public T getTile() {
        return tile;
    }
}

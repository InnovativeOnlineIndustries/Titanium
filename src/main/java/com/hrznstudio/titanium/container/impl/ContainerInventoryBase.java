/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.container.TitaniumContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContainerInventoryBase extends TitaniumContainerBase {
    private boolean isDisabled = false;
    private PlayerInventory inventory;
    private boolean hasPlayerInventory;
    private List<Integer> removableSlots;
    private List<Slot> slots;
    private boolean slotsPopulated;

    public ContainerInventoryBase(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(id, inventory, buffer);
    }

    public ContainerInventoryBase(ContainerType type, PlayerInventory inventory, int id) {
        super(type, id);
        this.inventory = inventory;
        this.removableSlots = new ArrayList<>();
        this.slots = new ArrayList<>();
        addPlayerChestInventory();
    }

    public ContainerInventoryBase(ContainerType type, PlayerInventory inventory, int id, IAssetProvider assetProvider) {
        super(type, id, assetProvider);
        this.inventory = inventory;
        this.removableSlots = new ArrayList<>();
        this.slots = new ArrayList<>();
        addPlayerChestInventory();
    }

    public void addPlayerChestInventory() {
        Point invPos = IAssetProvider.getAsset(getAssetProvider(), AssetTypes.BACKGROUND).getInventoryPosition();
        if (hasPlayerInventory) return;
        if (!slotsPopulated) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    this.removableSlots.add(addSlot(new DisableableSlot(inventory, j + i * 9 + 9, invPos.x + j * 18, invPos.y + i * 18, this)).slotNumber);
                }
            }
        } else {
            for (Slot slot : slots) {
                addRemovableSlot(addSlot(slot));
            }
        }

        hasPlayerInventory = true;
    }

    public void addRemovableSlot(Slot slot) {
        this.removableSlots.add(addSlot(slot).slotNumber);
    }

    public void removeSlots() {
        slots.addAll(this.inventorySlots);
        this.inventorySlots.removeIf(slot -> removableSlots.contains(slot.slotNumber));
        removableSlots.clear();
        hasPlayerInventory = false;
    }

    public PlayerInventory getPlayerInventory() {
        return inventory;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public boolean isDisabled() {
        return isDisabled;
    }
}

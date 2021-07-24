/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.impl;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.BasicContainer;
import com.hrznstudio.titanium.container.IDisableableContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.awt.*;

public class BasicInventoryContainer extends BasicContainer implements IDisableableContainer {
    private boolean isDisabled = false;
    private Inventory inventory;
    private boolean hasPlayerInventory;

    public BasicInventoryContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(id, inventory, buffer);
    }

    public BasicInventoryContainer(MenuType type, Inventory inventory, int id) {
        super(type, id);
        this.inventory = inventory;
        addPlayerChestInventory();
    }

    public BasicInventoryContainer(MenuType type, Inventory inventory, int id, IAssetProvider assetProvider) {
        super(type, id, assetProvider);
        this.inventory = inventory;
    }

    public void initInventory() {
        addExtraSlots();
        addPlayerChestInventory();
        addHotbarSlots();
    }

    public void addPlayerChestInventory() {
        Point invPos = IAssetProvider.getAsset(getAssetProvider(), AssetTypes.BACKGROUND).getInventoryPosition();
        if (!hasPlayerInventory) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlot(new DisableableSlot(inventory, j + i * 9 + 9, invPos.x + j * 18, invPos.y + i * 18, this));
                }
            }
            hasPlayerInventory = true;
        }
    }

    public void addHotbarSlots() {
        Point hotbarPos = IAssetProvider.getAsset(getAssetProvider(), AssetTypes.BACKGROUND).getHotbarPosition();
        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(getPlayerInventory(), k, hotbarPos.x + k * 18, hotbarPos.y));
        }
    }

    public Inventory getPlayerInventory() {
        return inventory;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}

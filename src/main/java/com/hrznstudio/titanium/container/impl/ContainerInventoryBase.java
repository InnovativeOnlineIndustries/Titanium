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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import java.awt.*;

public class ContainerInventoryBase extends TitaniumContainerBase {
    private boolean isDisabled = false;
    private PlayerInventory inventory;
    private boolean hasPlayerInventory;

    public ContainerInventoryBase(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(id, inventory, buffer);
    }

    public ContainerInventoryBase(ContainerType type, PlayerInventory inventory, int id) {
        super(type, id);
        this.inventory = inventory;
        addPlayerChestInventory();
    }

    public ContainerInventoryBase(ContainerType type, PlayerInventory inventory, int id, IAssetProvider assetProvider) {
        super(type, id, assetProvider);
        this.inventory = inventory;
        addPlayerChestInventory();
    }

    public void addPlayerChestInventory() {
        Point invPos = IAssetProvider.getAsset(getAssetProvider(), AssetTypes.BACKGROUND).getInventoryPosition();
        if (hasPlayerInventory) return;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new DisableableSlot(inventory, j + i * 9 + 9, invPos.x + j * 18, invPos.y + i * 18, this));
            }
        }
        hasPlayerInventory = true;
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

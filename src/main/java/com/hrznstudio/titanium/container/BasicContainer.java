/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BasicContainer extends AbstractContainerMenu {
    private IAssetProvider assetProvider;

    public BasicContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(inventory.player.containerMenu.getType(), id);
    }

    public BasicContainer(MenuType type, int id) {
        super(type, id);
        this.assetProvider = IAssetProvider.DEFAULT_PROVIDER;
    }

    public BasicContainer(MenuType type, int id, IAssetProvider assetProvider) {
        super(type, id);
        this.assetProvider = assetProvider;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotPos)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotPos);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int containerSlots = this.slots.size() - 9*4;
            if (slotPos < containerSlots) {
                if (!this.moveItemStackTo(itemstack1, containerSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public IAssetProvider getAssetProvider() {
        return assetProvider;
    }

    public void addExtraSlots() {

    }
}

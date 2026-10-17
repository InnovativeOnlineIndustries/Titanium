/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.apache.commons.lang3.tuple.Pair;

public class UpdatableSlotItemHandler extends ResourceHandlerSlot {

    private boolean enabled;
    private final ItemStacksResourceHandler itemHandler;

    public UpdatableSlotItemHandler(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.enabled = true;
    }

    public void update() {
        if (itemHandler instanceof InventoryComponent<?> inventory) {
            Pair<Integer, Integer> pos = inventory.getSlotPosition().apply(this.getSlotIndex());
            this.x = inventory.getXPos() + pos.getLeft();
            this.y = inventory.getYPos() + pos.getRight();
            this.enabled = inventory.getSlotVisiblePredicate().test(this.getSlotIndex());
        }
    }

    @Override
    public boolean isActive() {
        return enabled;
    }

    @Override
    public boolean mayPickup(Player player) {
        var resource = this.itemHandler.getResource(this.getSlotIndex());
        if (resource.isEmpty()) {
            return false;
        }
        if (itemHandler instanceof InventoryComponent<?> inventory) {
            return inventory.getExtractGUIPredicate().test(resource.toStack(), this.getSlotIndex());
        }
        return true;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (!super.mayPlace(stack)) {
            return false;
        }
        if (itemHandler instanceof InventoryComponent<?> inventory) {
            return inventory.getInsertGUIPredicate().test(stack, this.getSlotIndex());
        }
        return true;
    }
}

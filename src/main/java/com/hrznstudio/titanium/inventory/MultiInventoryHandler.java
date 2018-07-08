/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.inventory;

import com.hrznstudio.titanium.container.capability.IFacingHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MultiInventoryHandler {

    private final HashSet<PosInvHandler> inventoryHandlers;

    public MultiInventoryHandler() {
        this.inventoryHandlers = new HashSet<>();
    }

    public void addInventory(PosInvHandler invHandler) {
        this.inventoryHandlers.add(invHandler);
    }

    public MultiInvCapabilityHandler getCapabilityForSide(EnumFacing facing) {
        List<PosInvHandler> handlers = new ArrayList<>();
        for (PosInvHandler inventoryHandler : inventoryHandlers) {
            if (!(inventoryHandler instanceof IFacingHandler) || ((IFacingHandler) inventoryHandler).getFacingModes().containsKey(facing) && ((IFacingHandler) inventoryHandler).getFacingModes().get(facing).allowsConnection()) {
                handlers.add(inventoryHandler);
            }
        }
        return new MultiInvCapabilityHandler(handlers);
    }

    public HashSet<PosInvHandler> getInventoryHandlers() {
        return inventoryHandlers;
    }

    public static class MultiInvCapabilityHandler extends ItemStackHandler {

        private final List<PosInvHandler> inventoryHandlers;
        private int slotAmount;

        public MultiInvCapabilityHandler(List<PosInvHandler> inventoryHandlers) {
            this.inventoryHandlers = inventoryHandlers;
            this.slotAmount = 0;
            for (PosInvHandler inventoryHandler : this.inventoryHandlers) {
                slotAmount += inventoryHandler.getSlots();
            }
        }

        @Override
        public int getSlots() {
            return slotAmount;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            PosInvHandler handler = getFromSlot(slot);
            if (handler != null) {
                return handler.insertItem(getRelativeSlot(handler, slot), stack, simulate);
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            PosInvHandler handler = getFromSlot(slot);
            if (handler != null) {
                int relativeSlot = getRelativeSlot(handler, slot);
                if (!handler.getExtractPredicate().test(handler.getStackInSlot(relativeSlot), relativeSlot))
                    return ItemStack.EMPTY;
                return handler.extractItem(relativeSlot, amount, simulate);
            }
            return super.extractItem(slot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            PosInvHandler handler = getFromSlot(slot);
            if (handler != null) {
                return handler.getStackInSlot(getRelativeSlot(handler, slot));
            }
            return super.getStackInSlot(slot);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            PosInvHandler handler = getFromSlot(slot);
            if (handler != null) {
                handler.setStackInSlot(getRelativeSlot(handler, slot), stack);
            }
            super.setStackInSlot(slot, stack);
        }

        @Override
        protected void validateSlotIndex(int slot) {
            if (slot < 0 || slot >= slotAmount)
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + slotAmount + ")");
        }

        public PosInvHandler getFromSlot(int slot) {
            for (PosInvHandler handler : inventoryHandlers) {
                slot -= handler.getSlots();
                if (slot < 0) {
                    return handler;
                }
            }
            return null;
        }

        public int getRelativeSlot(PosInvHandler handler, int slot) {
            for (PosInvHandler h : inventoryHandlers) {
                if (h.equals(handler)) return slot;
                slot -= h.getSlots();
            }
            return 0;
        }
    }
}

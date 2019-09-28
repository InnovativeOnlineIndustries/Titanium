/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.sideness.ICapabilityHolder;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;


public class MultiInventoryHandler implements IGuiAddonProvider, ICapabilityHolder<PosInvHandler, MultiInventoryHandler.MultiInvCapabilityHandler> {

    private final LinkedHashSet<PosInvHandler> inventoryHandlers;
    private final HashMap<FacingUtil.Sideness, LazyOptional<MultiInvCapabilityHandler>> lazyOptionals;

    public MultiInventoryHandler() {
        this.inventoryHandlers = new LinkedHashSet<>();
        this.lazyOptionals = new HashMap<>();
        lazyOptionals.put(null, LazyOptional.empty());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            lazyOptionals.put(value, LazyOptional.empty());
        }
    }

    @Override
    public void add(PosInvHandler invHandler) {
        this.inventoryHandlers.add(invHandler);
        rebuildCapability(new FacingUtil.Sideness[]{null});
        rebuildCapability(FacingUtil.Sideness.values());
    }

    private void rebuildCapability(FacingUtil.Sideness[] sides) {
        for (FacingUtil.Sideness side : sides) {
            lazyOptionals.get(side).invalidate();
            lazyOptionals.put(side, LazyOptional.of(() -> new MultiInvCapabilityHandler(getHandlersForSide(side))));
        }
    }

    private List<PosInvHandler> getHandlersForSide(FacingUtil.Sideness sideness) {
        if (sideness == null) {
            return new ArrayList<>(inventoryHandlers);
        }
        List<PosInvHandler> handlers = new ArrayList<>();
        for (PosInvHandler inventoryHandler : inventoryHandlers) {
            if (inventoryHandler instanceof IFacingHandler) {
                if (((IFacingHandler) inventoryHandler).getFacingModes().containsKey(sideness) && ((IFacingHandler) inventoryHandler).getFacingModes().get(sideness).allowsConnection()) {
                    handlers.add(inventoryHandler);
                }
            } else {
                handlers.add(inventoryHandler);
            }
        }
        return handlers;
    }

    @Nonnull
    @Override
    public LazyOptional<MultiInvCapabilityHandler> getCapabilityForSide(FacingUtil.Sideness sideness) {
        return lazyOptionals.get(sideness);
    }

    @Override
    public boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, IFacingHandler.FaceMode mode) {
        for (PosInvHandler inventoryHandler : inventoryHandlers) {
            if (inventoryHandler.getName().equals(handlerName) && inventoryHandler instanceof IFacingHandler) {
                ((IFacingHandler) inventoryHandler).getFacingModes().put(facing, mode);
                rebuildCapability(new FacingUtil.Sideness[]{facing});
                return true;
            }
        }
        return false;
    }

    public HashSet<PosInvHandler> getInventoryHandlers() {
        return inventoryHandlers;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        inventoryHandlers.forEach(posInvHandler -> addons.addAll(posInvHandler.getGuiAddons()));
        return addons;
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
                if (handler.getInsertPredicate().test(stack, slot)) {
                    return handler.insertItem(getRelativeSlot(handler, slot), stack, simulate);
                } else {
                    return stack;
                }
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

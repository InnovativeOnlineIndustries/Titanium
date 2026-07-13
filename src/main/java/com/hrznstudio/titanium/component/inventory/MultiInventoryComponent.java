/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHandler;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.sideness.ICapabilityHolder;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.util.FacingUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nonnull;
import java.util.*;


public class MultiInventoryComponent<T extends IComponentHarness> implements IScreenAddonProvider, IContainerAddonProvider,
    ICapabilityHolder<MultiInventoryComponent.MultiInvCapabilityHandler<T>>, IComponentHandler {

    private final LinkedHashSet<InventoryComponent<T>> inventoryHandlers;
    private final Map<FacingUtil.Sideness, Optional<MultiInvCapabilityHandler<T>>> lazyOptionals;

    public MultiInventoryComponent() {
        this.inventoryHandlers = new LinkedHashSet<>();
        this.lazyOptionals = new HashMap<>();
        lazyOptionals.put(null, Optional.empty());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            lazyOptionals.put(value, Optional.empty());
        }
    }

    @Override
    public void add(Object... component) {
        Arrays.stream(component).filter(this::accepts).forEach(inventoryComponent -> {
            this.inventoryHandlers.add((InventoryComponent<T>) inventoryComponent);
            rebuildCapability(FacingUtil.Sideness.values());
        });
    }

    private boolean accepts(Object component) {
        return component instanceof InventoryComponent;
    }

    public void rebuildCapability(FacingUtil.Sideness[] sides) {
        for (FacingUtil.Sideness side : sides) {
            lazyOptionals.put(side, Optional.of(new MultiInvCapabilityHandler<>(getHandlersForSide(side))));
        }
    }

    private List<InventoryComponent<T>> getHandlersForSide(FacingUtil.Sideness sideness) {
        if (sideness == null) {
            return new ArrayList<>(inventoryHandlers);
        }
        List<InventoryComponent<T>> handlers = new ArrayList<>();
        for (InventoryComponent<T> inventoryHandler : inventoryHandlers) {
            if (inventoryHandler instanceof IFacingComponent) {
                if (((IFacingComponent) inventoryHandler).getFacingModes().containsKey(sideness) &&
                        ((IFacingComponent) inventoryHandler).getFacingModes().get(sideness).allowsConnection()) {
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
    public Optional<MultiInvCapabilityHandler<T>> getCapabilityForSide(FacingUtil.Sideness sideness) {
        return lazyOptionals.get(sideness);
    }

    @Override
    public boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, int mode) {
        for (InventoryComponent<T> inventoryHandler : inventoryHandlers) {
            if (inventoryHandler.getName().equals(handlerName) && inventoryHandler instanceof IFacingComponent) {
                ((IFacingComponent) inventoryHandler).getFacingModes().put(facing, ((IFacingComponent) inventoryHandler).getValidFacingModes()[mode]);
                rebuildCapability(new FacingUtil.Sideness[]{facing});
                return true;
            }
        }
        return false;
    }

    public HashSet<InventoryComponent<T>> getInventoryHandlers() {
        return inventoryHandlers;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        inventoryHandlers.forEach(posInvHandler -> addons.addAll(posInvHandler.getScreenAddons()));
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        for (InventoryComponent<T> inventoryComponent: inventoryHandlers) {
            addons.addAll(inventoryComponent.getContainerAddons());
        }
        return addons;
    }

    public static class MultiInvCapabilityHandler<T extends IComponentHarness> implements ResourceHandler<ItemResource> {

        private final List<InventoryComponent<T>> inventoryHandlers;
        private int slotAmount;

        public MultiInvCapabilityHandler(List<InventoryComponent<T>> inventoryHandlers) {
            this.inventoryHandlers = inventoryHandlers;
            this.slotAmount = 0;
            for (InventoryComponent<T> inventoryHandler : this.inventoryHandlers) {
                slotAmount += inventoryHandler.size();
            }
        }

        @Override
        public int size() {
            return slotAmount;
        }

        @Override
        public int insert(int slot, ItemResource resource, int amount, TransactionContext transaction) {
            InventoryComponent<T> handler = getFromSlot(slot);
            if (handler != null) {
                int relativeSlot = getRelativeSlot(handler, slot);
                return handler.insert(relativeSlot, resource, amount, transaction);
            }
            return 0;
        }

        @Override
        public int extract(int slot, ItemResource resource, int amount, TransactionContext transaction) {
            InventoryComponent<T> handler = getFromSlot(slot);
            if (handler != null) {
                int relativeSlot = getRelativeSlot(handler, slot);
                return handler.extract(relativeSlot, resource, amount, transaction);
            }
            return 0;
        }

        @Override
        public ItemResource getResource(int slot) {
            InventoryComponent<T> handler = getFromSlot(slot);
            if (handler != null) {
                return handler.getResource(getRelativeSlot(handler, slot));
            }
            return ItemResource.EMPTY;
        }

        @Override
        public long getAmountAsLong(int slot) {
            InventoryComponent<T> handler = getFromSlot(slot);
            return handler == null ? 0 : handler.getAmountAsLong(getRelativeSlot(handler, slot));
        }

        @Override
        public long getCapacityAsLong(int slot, ItemResource resource) {
            InventoryComponent<T> handler = getFromSlot(slot);
            return handler == null ? 0 : handler.getCapacityAsLong(getRelativeSlot(handler, slot), resource);
        }

        @Override
        public boolean isValid(int slot, ItemResource resource) {
            InventoryComponent<T> handler = getFromSlot(slot);
            return handler != null && handler.isValid(getRelativeSlot(handler, slot), resource);
        }

        public InventoryComponent<T> getFromSlot(int slot) {
            for (InventoryComponent<T> handler : inventoryHandlers) {
                slot -= handler.size();
                if (slot < 0) {
                    return handler;
                }
            }
            return null;
        }

        public int getRelativeSlot(InventoryComponent<T> handler, int slot) {
            for (InventoryComponent<T> h : inventoryHandlers) {
                if (h.equals(handler)) return slot;
                slot -= h.size();
            }
            return 0;
        }
    }
}

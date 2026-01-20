/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;

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

    public static class MultiInvCapabilityHandler<T extends IComponentHarness> extends ItemStackHandler {

        private final List<InventoryComponent<T>> inventoryHandlers;
        // Кеш для быстрого поиска handler по слоту - O(1) вместо O(n)
        private final InventoryComponent<T>[] slotToHandler;
        private final int[] slotToRelativeSlot;
        private final int slotAmount;

        @SuppressWarnings("unchecked")
        public MultiInvCapabilityHandler(List<InventoryComponent<T>> inventoryHandlers) {
            this.inventoryHandlers = inventoryHandlers;

            // Подсчитываем общее количество слотов
            int totalSlots = 0;
            for (InventoryComponent<T> handler : inventoryHandlers) {
                totalSlots += handler.getSlots();
            }
            this.slotAmount = totalSlots;

            // Предварительно вычисляем mapping слотов - избегаем линейного поиска каждый раз
            this.slotToHandler = new InventoryComponent[totalSlots];
            this.slotToRelativeSlot = new int[totalSlots];

            int globalSlot = 0;
            for (InventoryComponent<T> handler : inventoryHandlers) {
                int handlerSlots = handler.getSlots();
                for (int localSlot = 0; localSlot < handlerSlots; localSlot++) {
                    slotToHandler[globalSlot] = handler;
                    slotToRelativeSlot[globalSlot] = localSlot;
                    globalSlot++;
                }
            }
        }

        @Override
        public int getSlots() {
            return slotAmount;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot < 0 || slot >= slotAmount) return stack;
            InventoryComponent<T> handler = slotToHandler[slot];
            int relativeSlot = slotToRelativeSlot[slot];
            if (handler.getInsertPredicate().test(stack, relativeSlot)) {
                return handler.insertItem(relativeSlot, stack, simulate);
            }
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < 0 || slot >= slotAmount) return ItemStack.EMPTY;
            InventoryComponent<T> handler = slotToHandler[slot];
            int relativeSlot = slotToRelativeSlot[slot];
            if (!handler.getExtractPredicate().test(handler.getStackInSlot(relativeSlot), relativeSlot)) {
                return ItemStack.EMPTY;
            }
            return handler.extractItem(relativeSlot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot < 0 || slot >= slotAmount) return ItemStack.EMPTY;
            return slotToHandler[slot].getStackInSlot(slotToRelativeSlot[slot]);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            if (slot < 0 || slot >= slotAmount) return;
            slotToHandler[slot].setStackInSlot(slotToRelativeSlot[slot], stack);
        }

        @Override
        protected void validateSlotIndex(int slot) {
            if (slot < 0 || slot >= slotAmount)
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + slotAmount + ")");
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot < 0 || slot >= slotAmount) return 0;
            return slotToHandler[slot].getSlotLimit(slotToRelativeSlot[slot]);
        }

        // Оставляем для обратной совместимости
        public InventoryComponent<T> getFromSlot(int slot) {
            if (slot < 0 || slot >= slotAmount) return null;
            return slotToHandler[slot];
        }

        public int getRelativeSlot(InventoryComponent<T> handler, int slot) {
            if (slot < 0 || slot >= slotAmount) return 0;
            return slotToRelativeSlot[slot];
        }
    }
}

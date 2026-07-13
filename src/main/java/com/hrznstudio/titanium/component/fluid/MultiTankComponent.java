/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.fluid;

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
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MultiTankComponent<T extends IComponentHarness> implements IScreenAddonProvider, IContainerAddonProvider,
    ICapabilityHolder<MultiTankComponent.MultiTankCapabilityHandler<T>>, IComponentHandler {

    private final LinkedHashSet<FluidTankComponent<T>> tanks;
    private final HashMap<FacingUtil.Sideness, Optional<MultiTankCapabilityHandler<T>>> lazyOptionals;

    public MultiTankComponent() {
        tanks = new LinkedHashSet<>();
        this.lazyOptionals = new HashMap<>();
        lazyOptionals.put(null, Optional.empty());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            lazyOptionals.put(value, Optional.empty());
        }
    }

    @Override
    public void add(Object... component) {
        Arrays.stream(component).filter(this::accepts).forEach(tank -> {
            this.tanks.add((FluidTankComponent<T>) tank);
            rebuildCapability(new FacingUtil.Sideness[]{null});
            rebuildCapability(FacingUtil.Sideness.values());
        });
    }

    private boolean accepts(Object component) {
        return component instanceof FluidTankComponent;
    }

    public void rebuildCapability(FacingUtil.Sideness[] sides) {
        for (FacingUtil.Sideness side : sides) {
            lazyOptionals.put(side, Optional.of(new MultiTankCapabilityHandler<>(getHandlersForSide(side))));
        }
    }

    private List<FluidTankComponent<T>> getHandlersForSide(FacingUtil.Sideness sideness) {
        if (sideness == null) {
            return new ArrayList<>(tanks);
        }
        List<FluidTankComponent<T>> handlers = new ArrayList<>();
        for (FluidTankComponent<T> tankHandler : tanks) {
            if (tankHandler instanceof IFacingComponent) {
                if (((IFacingComponent) tankHandler).getFacingModes().containsKey(sideness) &&
                        ((IFacingComponent) tankHandler).getFacingModes().get(sideness).allowsConnection()) {
                    handlers.add(tankHandler);
                }
            } else {
                handlers.add(tankHandler);
            }
        }
        return handlers;
    }

    @Nonnull
    @Override
    public Optional<MultiTankCapabilityHandler<T>> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness) {
        return lazyOptionals.get(sideness);
    }

    @Override
    public boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, int mode) {
        for (FluidTankComponent<T> tankHandler : tanks) {
            if (tankHandler.getName().equals(handlerName) && tankHandler instanceof IFacingComponent) {
                ((IFacingComponent) tankHandler).getFacingModes().put(facing, ((IFacingComponent) tankHandler).getValidFacingModes()[mode]);
                rebuildCapability(new FacingUtil.Sideness[]{facing});
                return true;
            }
        }
        return false;
    }

    public HashSet<FluidTankComponent<T>> getTanks() {
        return tanks;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        for (FluidTankComponent<T> tank : tanks) {
            addons.addAll(tank.getScreenAddons());
        }
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        for (FluidTankComponent<T> tank : tanks) {
            addons.addAll(tank.getContainerAddons());
        }
        return addons;
    }

    public static class MultiTankCapabilityHandler<T extends IComponentHarness> implements ResourceHandler<FluidResource> {

        private final List<FluidTankComponent<T>> tanks;

        public MultiTankCapabilityHandler(List<FluidTankComponent<T>> tanks) {
            this.tanks = tanks;
        }

        public boolean isEmpty() {
            return tanks.isEmpty();
        }

        @Override
        public int size() {
            return tanks.size();
        }

        @Override
        public FluidResource getResource(int tank) {
            return tanks.get(tank).getResource(0);
        }

        @Override
        public long getAmountAsLong(int tank) {
            return tanks.get(tank).getAmountAsLong(0);
        }

        @Override
        public long getCapacityAsLong(int tank, FluidResource resource) {
            return tanks.get(tank).getCapacityAsLong(0, resource);
        }

        @Override
        public boolean isValid(int tank, FluidResource resource) {
            return tanks.get(tank).isValid(0, resource);
        }

        @Override
        public int insert(int tank, FluidResource resource, int amount, TransactionContext transaction) {
            return tanks.get(tank).insert(0, resource, amount, transaction);
        }

        @Override
        public int extract(int tank, FluidResource resource, int amount, TransactionContext transaction) {
            return tanks.get(tank).extract(0, resource, amount, transaction);
        }
    }
}

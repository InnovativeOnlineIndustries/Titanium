/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MultiTankComponent<T extends IComponentHarness> implements IScreenAddonProvider, IContainerAddonProvider,
    ICapabilityHolder<FluidTankComponent<T>, MultiTankComponent.MultiTankCapabilityHandler<T>>, IComponentHandler<FluidTankComponent<T>> {

    private final LinkedHashSet<FluidTankComponent<T>> tanks;
    private final HashMap<FacingUtil.Sideness, LazyOptional<MultiTankCapabilityHandler<T>>> lazyOptionals;

    public MultiTankComponent() {
        tanks = new LinkedHashSet<>();
        this.lazyOptionals = new HashMap<>();
        lazyOptionals.put(null, LazyOptional.empty());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            lazyOptionals.put(value, LazyOptional.empty());
        }
    }

    @Override
    public void add(@Nonnull FluidTankComponent<T> tank) {
        this.tanks.add(tank);
        rebuildCapability(new FacingUtil.Sideness[]{null});
        rebuildCapability(FacingUtil.Sideness.values());
    }

    @Override
    public boolean accepts(Object component) {
        return component instanceof FluidTankComponent;
    }

    private void rebuildCapability(FacingUtil.Sideness[] sides) {
        for (FacingUtil.Sideness side : sides) {
            lazyOptionals.get(side).invalidate();
            lazyOptionals.put(side, LazyOptional.of(() -> new MultiTankCapabilityHandler<>(getHandlersForSide(side))));
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
    public LazyOptional<MultiTankCapabilityHandler<T>> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness) {
        return lazyOptionals.get(sideness);
    }

    @Override
    public boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, IFacingComponent.FaceMode mode) {
        for (FluidTankComponent<T> tankHandler : tanks) {
            if (tankHandler.getName().equals(handlerName) && tankHandler instanceof IFacingComponent) {
                ((IFacingComponent) tankHandler).getFacingModes().put(facing, mode);
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

    public static class MultiTankCapabilityHandler<T extends IComponentHarness> implements IFluidHandler {

        private final List<FluidTankComponent<T>> tanks;

        public MultiTankCapabilityHandler(List<FluidTankComponent<T>> tanks) {
            this.tanks = tanks;
        }

        public boolean isEmpty() {
            return tanks.isEmpty();
        }

        @Override
        public int getTanks() {
            return tanks.size();
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return tanks.get(tank).getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tanks.get(tank).getTankCapacity(tank);
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return tanks.get(tank).isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            for (FluidTankComponent<T> tank : tanks) {
                if (tank.fill(resource, FluidAction.SIMULATE) != 0) {
                    return tank.fill(resource, action);
                }
            }
            return 0;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            for (FluidTankComponent<T> tank : tanks) {
                if (!tank.drain(resource, FluidAction.SIMULATE).isEmpty()) {
                    return tank.drain(resource, action);
                }
            }
            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            for (FluidTankComponent<T> tank : tanks) {
                if (!tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty()) {
                    return tank.drain(maxDrain, action);
                }
            }
            return FluidStack.EMPTY;
        }
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.sideness.ICapabilityHolder;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MultiTankHandler implements IGuiAddonProvider, ICapabilityHolder<PosFluidTank, MultiTankHandler.MultiTankCapabilityHandler> {

    private final LinkedHashSet<PosFluidTank> tanks;
    private final HashMap<FacingUtil.Sideness, LazyOptional<MultiTankCapabilityHandler>> lazyOptionals;

    public MultiTankHandler() {
        tanks = new LinkedHashSet<>();
        this.lazyOptionals = new HashMap<>();
        lazyOptionals.put(null, LazyOptional.empty());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            lazyOptionals.put(value, LazyOptional.empty());
        }
    }

    @Override
    public void add(PosFluidTank tank) {
        this.tanks.add(tank);
        rebuildCapability(new FacingUtil.Sideness[]{null});
        rebuildCapability(FacingUtil.Sideness.values());
    }

    private void rebuildCapability(FacingUtil.Sideness[] sides) {
        for (FacingUtil.Sideness side : sides) {
            lazyOptionals.get(side).invalidate();
            lazyOptionals.put(side, LazyOptional.of(() -> new MultiTankCapabilityHandler(getHandlersForSide(side))));
        }
    }

    private List<PosFluidTank> getHandlersForSide(FacingUtil.Sideness sideness) {
        if (sideness == null) {
            return new ArrayList<>(tanks);
        }
        List<PosFluidTank> handlers = new ArrayList<>();
        for (PosFluidTank tankHandler : tanks) {
            if (tankHandler instanceof IFacingHandler) {
                if (((IFacingHandler) tankHandler).getFacingModes().containsKey(sideness) && ((IFacingHandler) tankHandler).getFacingModes().get(sideness).allowsConnection()) {
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
    public LazyOptional<MultiTankCapabilityHandler> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness) {
        return lazyOptionals.get(sideness);
    }

    @Override
    public boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, IFacingHandler.FaceMode mode) {
        for (PosFluidTank tankHandler : tanks) {
            if (tankHandler.getName().equals(handlerName) && tankHandler instanceof IFacingHandler) {
                ((IFacingHandler) tankHandler).getFacingModes().put(facing, mode);
                rebuildCapability(new FacingUtil.Sideness[]{facing});
                return true;
            }
        }
        return false;
    }

    public HashSet<PosFluidTank> getTanks() {
        return tanks;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        for (PosFluidTank tank : tanks) {
            addons.addAll(tank.getGuiAddons());
        }
        return addons;
    }

    public static class MultiTankCapabilityHandler implements IFluidHandler {

        private final List<PosFluidTank> tanks;

        public MultiTankCapabilityHandler(List<PosFluidTank> tanks) {
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
            for (PosFluidTank tank : tanks) {
                if (tank.fill(resource, FluidAction.SIMULATE) != 0)
                    return tank.fill(resource, action);
            }
            return 0;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            for (PosFluidTank tank : tanks) {
                if (!tank.drain(resource, FluidAction.SIMULATE).isEmpty())
                    return tank.drain(resource, action);
            }
            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            for (PosFluidTank tank : tanks) {
                if (!tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty())
                    return tank.drain(maxDrain, action);
            }
            return FluidStack.EMPTY;
        }
    }
}

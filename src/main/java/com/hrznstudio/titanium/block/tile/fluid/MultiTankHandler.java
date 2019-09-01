/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class MultiTankHandler implements IGuiAddonProvider {

    private LinkedHashSet<PosFluidTank> tanks;

    public MultiTankHandler() {
        tanks = new LinkedHashSet<>();
    }

    public void addTank(PosFluidTank tank) {
        this.tanks.add(tank);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        for (PosFluidTank tank : tanks) {
            addons.addAll(tank.getGuiAddons());
        }
        return addons;
    }

    public MultiTankCapabilityHandler getCapabilityForSide(FacingUtil.Sideness sideness) {
        if (sideness == null)
            return new MultiTankCapabilityHandler(new ArrayList<>(tanks));
        List<PosFluidTank> tanks = new ArrayList<>();
        for (PosFluidTank tank : this.tanks) {
            if (tank instanceof IFacingHandler) {
                if (((IFacingHandler) tank).getFacingModes().containsKey(sideness) && ((IFacingHandler) tank).getFacingModes().get(sideness).allowsConnection()) {
                    tanks.add(tank);
                }
            } else {
                tanks.add(tank);
            }

        }
        return new MultiTankCapabilityHandler(tanks);
    }

    public HashSet<PosFluidTank> getTanks() {
        return tanks;
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
            return 0;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            for (PosFluidTank tank : tanks) {
                if (tank.drain(resource, action).isEmpty())
                    return tank.drain(resource, action);
            }
            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            for (PosFluidTank tank : tanks) {
                if (tank.drain(maxDrain, action).isEmpty())
                    return tank.drain(maxDrain, action);
            }
            return FluidStack.EMPTY;
        }
    }
}

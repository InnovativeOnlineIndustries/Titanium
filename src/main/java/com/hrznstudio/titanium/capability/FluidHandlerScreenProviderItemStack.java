/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FluidHandlerScreenProviderItemStack extends FluidHandlerItemStack implements IScreenAddonProvider, IFluidTank {

    public FluidHandlerScreenProviderItemStack(@Nonnull ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return new ArrayList<>();
    }

    @Override
    public int getFluidAmount() {
        return this.getFluid().getAmount();
    }

    @Override
    public int getCapacity() {
        return this.getTankCapacity(1);
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return this.isFluidValid(1, stack);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 * This class exists because @{@link FluidUtil}'s tryEmptyContainer doesn't work properly
 */
public class TitaniumFluidUtil {

    @Nonnull
    public static FluidActionResult tryEmptyContainer(@Nonnull ItemStack container, IFluidHandler fluidDestination, int maxAmount, boolean doDrain) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1);
        return FluidUtil.getFluidHandler(containerCopy)
            .map(containerFluidHandler -> {
                FluidStack transfer = FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, doDrain);
                if (transfer.isEmpty()) {
                    return FluidActionResult.FAILURE;
                }
                ItemStack resultContainer = containerFluidHandler.getContainer();
                return new FluidActionResult(resultContainer);
            })
            .orElse(FluidActionResult.FAILURE);
    }

}

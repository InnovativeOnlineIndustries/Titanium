/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.energy.EnergyStorageItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

//import net.minecraft.util.text.TextFormatting;

public class EnergyItem extends BasicItem {
    private final int capacity;
    private final int input;
    private final int output;

    public EnergyItem(String name, int capacity, int input, int output, Properties properties) {
        super(name, properties.maxStackSize(1));
        this.capacity = capacity;
        this.input = input;
        this.output = output;
    }

    public EnergyItem(String name, Properties properties, int capacity, int throughput) {
        this(name, capacity, throughput, throughput, properties);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getInput() {
        return input;
    }

    public int getOutput() {
        return output;
    }

    @Override
    public boolean hasTooltipDetails(@Nullable Key key) {
        return key == Key.SHIFT || super.hasTooltipDetails(key);
    }

    @Override
    public void addTooltipDetails(@Nullable Key key, @Nonnull ItemStack stack, @Nonnull List<ITextComponent> tooltip, boolean advanced) {
        super.addTooltipDetails(key, stack, tooltip, advanced);
        if (key == Key.SHIFT) {
            //getEnergyStorage(stack).ifPresent(storage -> tooltip.add(new TextComponentString(TextFormatting.YELLOW + "Energy: " + TextFormatting.RED + storage.getEnergyStored() + TextFormatting.YELLOW + "/" + TextFormatting.RED + storage.getMaxEnergyStored() + TextFormatting.RESET))); TODO
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getEnergyStorage(stack).isPresent();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return getEnergyStorage(stack).map(storage -> 1 - (double) storage.getEnergyStored() / (double) storage.getMaxEnergyStored()).orElse(0.0);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x00E93232;
    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProvider(new EnergyStorageItemStack(stack, capacity, input, output));
    }

    public static class CapabilityProvider implements ICapabilityProvider {
        private LazyOptional<IEnergyStorage> energyCap;

        public CapabilityProvider(EnergyStorageItemStack energy) {
            this.energyCap = LazyOptional.of(() -> energy);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityEnergy.ENERGY) {
                return energyCap.cast();
            }
            return LazyOptional.empty();
        }
    }
}
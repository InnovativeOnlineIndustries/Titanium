package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.energy.EnergyStorageItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemEnergy extends ItemBase {
    private final int capacity;
    private final int input;
    private final int output;

    public ItemEnergy(String name, int capacity, int input, int output) {
        super(name);
        this.capacity = capacity;
        this.input = input;
        this.output = output;
    }

    public ItemEnergy(String name, int capacity, int throughput) {
        this(name, capacity, throughput, throughput);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityProvider(new EnergyStorageItemStack(stack, 0, capacity, input, output));
    }

    public static class CapabilityProvider implements ICapabilityProvider {
        private EnergyStorageItemStack energy;

        public CapabilityProvider(EnergyStorageItemStack energy) {
            this.energy = energy;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(energy) : null;
        }
    }
}

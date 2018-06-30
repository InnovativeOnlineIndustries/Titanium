package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.energy.EnergyStorageItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

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

    public Optional<IEnergyStorage> getEnergyStorage(ItemStack stack){
        return Optional.ofNullable(stack.getCapability(CapabilityEnergy.ENERGY,null));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityProvider(new EnergyStorageItemStack(stack, capacity, input, output));
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

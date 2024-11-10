/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.energy.EnergyStorageItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EnergyItem extends BasicItem {
    private final int capacity;
    private final int input;
    private final int output;

    public EnergyItem(String name, int capacity, int input, int output, Properties properties) {
        super(name, properties.stacksTo(1));
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
    public void addTooltipDetails(@Nullable Key key, @Nonnull ItemStack stack, @Nonnull List<Component> tooltip, boolean advanced) {
        super.addTooltipDetails(key, stack, tooltip, advanced);
        if (key == Key.SHIFT) {
            getEnergyStorage(stack).ifPresent(storage ->
                tooltip.add(
                    Component.empty().withStyle(ChatFormatting.YELLOW)
                        .append(Component.translatable("tooltip.titanium.energy").getString()).withStyle(ChatFormatting.RED)
                        .append(String.valueOf(storage.getEnergyStored())).withStyle(ChatFormatting.YELLOW)
                        .append("/").withStyle(ChatFormatting.RED)
                        .append(String.valueOf(storage.getMaxEnergyStored())).withStyle(ChatFormatting.RESET)));
        }
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getEnergyStorage(stack).isPresent();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.round(getEnergyStorage(stack).map(storage -> 1 - (double) storage.getEnergyStored() / (double) storage.getMaxEnergyStored()).orElse(0.0) * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00E93232;
    }

    public Optional<IEnergyStorage> getEnergyStorage(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM, null));
    }

    public IEnergyStorage initEnergy(ItemStack stack) {
        return new EnergyStorageItemStack(stack);
    }

}

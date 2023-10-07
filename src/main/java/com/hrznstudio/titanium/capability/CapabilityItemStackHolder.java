/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.api.capability.IStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityItemStackHolder {

    @CapabilityInject(IStackHolder.class)
    public static Capability<IStackHolder> ITEMSTACK_HOLDER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IStackHolder.class, new DefaultStorage<>(), () -> new ItemStackHolderCapability(() -> ItemStack.EMPTY));
    }

    private static class DefaultStorage<T extends IStackHolder> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
            if (!(instance instanceof ItemStackHolderCapability))
                throw new RuntimeException("Cannot serialize to an instance that isn't the default implementation");
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("ItemStack", instance.getHolder().get().write(new CompoundNBT()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            if (!(instance instanceof ItemStackHolderCapability))
                throw new RuntimeException("Cannot serialize to an instance that isn't the default implementation");
            instance.setHolder(() -> ItemStack.read(((CompoundNBT) nbt).getCompound("ItemStack")));
        }
    }

}

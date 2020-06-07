package com.hrznstudio.titanium.api.capability;

import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public interface IStackHolder {

    ItemStack getHolder();

    void setHolder(Supplier<ItemStack> stack);

}

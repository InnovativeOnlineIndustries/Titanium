/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.filter;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.ItemstackFilterGuiAddon;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemstackFilter implements IFilter<ItemStack> {

    private static FilterAction<ItemStack> SIMPLE = new FilterAction<>((itemStackIFilter, stack) -> Arrays.stream(itemStackIFilter.getFilter()).anyMatch(itemStackFilterSlot -> stack.isItemEqual(itemStackFilterSlot.getFilter())));
    private static FilterAction<ItemStack> IGNORE_DURABILITY = new FilterAction<>((itemStackIFilter, stack) -> Arrays.stream(itemStackIFilter.getFilter()).anyMatch(itemStackFilterSlot -> stack.isItemEqualIgnoreDurability(itemStackFilterSlot.getFilter())));
    private static FilterAction<ItemStack> DURABILITY_LESS_50 = new FilterAction<>((itemStackIFilter, stack) -> Arrays.stream(itemStackIFilter.getFilter()).anyMatch(itemStackFilterSlot -> stack.isItemEqual(itemStackFilterSlot.getFilter())) && stack.getDamage() < stack.getMaxDamage() / 50);
    private static FilterAction<ItemStack> DAMAGED = new FilterAction<>((itemStackIFilter, stack) -> Arrays.stream(itemStackIFilter.getFilter()).anyMatch(itemStackFilterSlot -> stack.isItemEqual(itemStackFilterSlot.getFilter())) && stack.getDamage() < stack.getMaxDamage());
    private static FilterAction<ItemStack> NOT_DAMAGED = new FilterAction<>((itemStackIFilter, stack) -> Arrays.stream(itemStackIFilter.getFilter()).anyMatch(itemStackFilterSlot -> stack.isItemEqual(itemStackFilterSlot.getFilter())) && stack.getDamage() == stack.getMaxDamage());
    private static FilterAction<ItemStack> DURABILITY_MORE_50 = new FilterAction<>((itemStackIFilter, stack) -> Arrays.stream(itemStackIFilter.getFilter()).anyMatch(itemStackFilterSlot -> stack.isItemEqual(itemStackFilterSlot.getFilter())) && stack.getDamage() > stack.getMaxDamage() / 50);
    private static FilterAction<ItemStack>[] ACTIONS = new FilterAction[]{SIMPLE, IGNORE_DURABILITY, DURABILITY_LESS_50, DAMAGED, NOT_DAMAGED, DURABILITY_MORE_50};
    private final FilterSlot<ItemStack>[] filter;

    private Type type;
    private int pointer;
    private String name;

    public ItemstackFilter(String name, int filterSize) {
        this.name = name;
        this.filter = new FilterSlot[filterSize];
        this.type = Type.WHITELIST;
        this.pointer = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean acceptsAsFilter(ItemStack filter) {
        return true;
    }

    @Override
    public void setFilter(int slot, ItemStack stack) {
        filter[slot].setFilter(stack);
    }

    @Override
    public void setFilter(int slot, FilterSlot<ItemStack> filterSlot) {
        this.filter[slot] = filterSlot;
    }

    @Override
    public FilterSlot<ItemStack>[] getFilter() {
        return filter;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void toggleFilterMode() {
        if (this.type.equals(Type.WHITELIST)) {
            type = Type.BLACKLIST;
        } else {
            type = Type.WHITELIST;
        }
    }

    @Override
    public void selectNextFilter() {
        pointer = (pointer + 1) % ACTIONS.length;
    }

    @Override
    public FilterAction<ItemStack> getAction() {
        return ACTIONS[pointer];
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("Pointer", pointer);
        CompoundNBT filter = new CompoundNBT();
        for (FilterSlot<ItemStack> itemStackFilterSlot : this.filter) {
            if (itemStackFilterSlot != null && !itemStackFilterSlot.getFilter().isEmpty())
                filter.put(itemStackFilterSlot.getFilterID() + "", itemStackFilterSlot.getFilter().serializeNBT());
        }
        compoundNBT.put("Filter", filter);
        compoundNBT.putString("Type", type.name());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        pointer = nbt.getInt("Pointer");
        CompoundNBT filter = nbt.getCompound("Filter");
        for (FilterSlot<ItemStack> filterSlot : this.filter) {
            filterSlot.setFilter(ItemStack.EMPTY);
        }
        for (String key : filter.keySet()) {
            this.filter[Integer.parseInt(key)].setFilter(ItemStack.read(filter.getCompound(key)));
        }
        this.type = Type.valueOf(nbt.getString("Type"));
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> list = new ArrayList<>();
        list.add(() -> new ItemstackFilterGuiAddon(this));
        return list;
    }
}

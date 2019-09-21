package com.hrznstudio.titanium.api.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;

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

    public ItemstackFilter(int filterSize) {
        this.filter = new FilterSlot[filterSize];
        this.type = Type.WHITELIST;
        this.pointer = 0;
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
        for (String key : filter.keySet()) {
            this.filter[Integer.getInteger(key)].setFilter(ItemStack.read(filter.getCompound(key)));
        }
        this.type = Type.valueOf(nbt.getString("Type"));
    }
}

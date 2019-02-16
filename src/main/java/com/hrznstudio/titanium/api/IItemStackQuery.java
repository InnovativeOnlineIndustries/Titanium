/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface IItemStackQuery extends Predicate<ItemStack> {

    IItemStackQuery ANYTHING = stack -> true;
    IItemStackQuery NOTHING = stack -> false;

    static IItemStackQuery of(Item item) {
        return new ItemQuery(item);
    }

    static IItemStackQuery of(ItemStack stack) {
        return new ItemStackQuery(stack);
    }

    static IItemStackQuery of(Block block) {
        return of(Item.getItemFromBlock(block));
    }

    boolean matches(ItemStack stack);

    @Override
    default boolean test(ItemStack stack) {
        return matches(stack);
    }

    default BiPredicate<ItemStack, Integer> toSlotFilter(int... slots) {
        return toSlotFilter((slot) -> ArrayUtils.contains(slots, slot));
    }

    default BiPredicate<ItemStack, Integer> toSlotFilter(Predicate<Integer> slotPredicate) {
        return (stack, slot) -> slotPredicate.test(slot) && IItemStackQuery.this.test(stack);
    }

    default BiPredicate<ItemStack, Integer> toSlotFilter(int min, int max) {
        return toSlotFilter(slot -> slot >= min && slot <= max);
    }

    default BiPredicate<ItemStack, Integer> toSlotFilter() {
        return toSlotFilter(slot -> true);
    }

    interface IItemStackQueryRecipe extends IItemStackQuery {
        ItemStack[] getMatchingStacks();
    }

    @FunctionalInterface
    interface IItemStackNBTQuery extends IItemStackQuery {
        @Override
        default boolean matches(ItemStack itemStack) {
            return itemStack.hasTag() && matches(itemStack.getTag());
        }

        boolean matches(NBTTagCompound compound);
    }

    class ItemStackQuery implements IItemStackQueryRecipe {
        protected ItemStack stack;

        public ItemStackQuery(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean matches(ItemStack stack) {
            return ItemStack.areItemStacksEqual(this.stack, stack);
        }

        @Override
        public ItemStack[] getMatchingStacks() {
            return new ItemStack[]{stack};
        }
    }

    class ItemQuery implements IItemStackQueryRecipe {

        protected Item item;

        public ItemQuery(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return this.item;
        }

        @Override
        public ItemStack[] getMatchingStacks() {
            return new ItemStack[]{new ItemStack(item)};
        }

        @Override
        public boolean matches(ItemStack itemStack) {
            return itemStack.getItem().equals(this.item);
        }
    }

    class IngredientQuery implements IItemStackQueryRecipe {

        protected Ingredient ingredient;

        public IngredientQuery(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public ItemStack[] getMatchingStacks() {
            return ingredient.getMatchingStacks();
        }

        public Ingredient getIngredient() {
            return ingredient;
        }

        @Override
        public boolean matches(ItemStack itemStack) {
            return this.ingredient.test(itemStack);
        }
    }
}
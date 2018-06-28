/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Predicate;

@FunctionalInterface
public interface IItemStackQuery extends Predicate<ItemStack> {

    IItemStackQuery ANYTHING = stack -> true;
    IItemStackQuery NOTHING = stack -> false;

    boolean matches(ItemStack stack);

    @Override
    default boolean test(ItemStack stack) {
        return matches(stack);
    }

    static IItemStackQuery of(Item item) {
        return new ItemQuery(item);
    }

    static IItemStackQuery of(ItemStack stack) {
        return new ItemStackQuery(stack);
    }

    static IItemStackQuery of(Block block) {
        return of(Item.getItemFromBlock(block));
    }

    interface IItemStackQueryRecipe extends IItemStackQuery {
        ItemStack[] getMatchingStacks();
    }

    @FunctionalInterface
    interface IItemStackNBTQuery extends IItemStackQuery {
        @Override
        default boolean matches(ItemStack itemStack) {
            return matches(itemStack.getTagCompound());
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
            return this.ingredient.apply(itemStack);
        }
    }

    class ItemMetaQuery extends ItemQuery {

        protected int meta;

        public ItemMetaQuery(Item item, int meta) {
            super(item);
            this.meta = meta;
        }

        @Override
        public ItemStack[] getMatchingStacks() {
            return new ItemStack[]{new ItemStack(item, 1, meta)};
        }

        public int getMeta() {
            return this.meta;
        }

        @Override
        public boolean matches(ItemStack itemStack) {
            return super.matches(itemStack) && itemStack.getMetadata() == this.meta;
        }
    }
}
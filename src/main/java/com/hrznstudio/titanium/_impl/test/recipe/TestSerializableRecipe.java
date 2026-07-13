/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class TestSerializableRecipe implements Recipe<CraftingInput> {

    public static Holder<RecipeSerializer<?>> SERIALIZER;
    public static Holder<RecipeType<?>> RECIPE_TYPE;
    public static final List<TestSerializableRecipe> RECIPES = new ArrayList<>();
    public static final MapCodec<TestSerializableRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
        Ingredient.CODEC.fieldOf("input").forGetter(i -> i.input),
        ItemStack.CODEC.fieldOf("output").forGetter(i -> i.output),
        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(i -> i.block)
    ).apply(in, TestSerializableRecipe::new));
    private static boolean examplesInitialized;

    public static synchronized void initializeExamples() {
        if (examplesInitialized) {
            return;
        }
        examplesInitialized = true;
        new TestSerializableRecipe(Ingredient.of(Items.OAK_SAPLING), new ItemStack(Items.STICK, 3), Blocks.STONE);
        new TestSerializableRecipe(Ingredient.of(Blocks.DIRT), new ItemStack(Items.DIAMOND, 1), Blocks.DIRT);
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
        pick.setDamageValue(100);
        new TestSerializableRecipe(Ingredient.of(Blocks.STONE), pick, Blocks.DIRT);
    }

    public Ingredient input;
    public ItemStack output;
    public Block block;

    public TestSerializableRecipe(Ingredient input, ItemStack output, Block block) {
        this.input = input;
        this.output = output;
        this.block = block;
        RECIPES.add(this);
    }

    public TestSerializableRecipe() {
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        return this.input.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return this.output.copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(input);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return (RecipeSerializer<? extends Recipe<CraftingInput>>) SERIALIZER.value();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return (RecipeType<? extends Recipe<CraftingInput>>) RECIPE_TYPE.value();
    }

    public boolean isValid(ItemStack input, Block block) {
        return this.input.test(input) && this.block == block;
    }
}

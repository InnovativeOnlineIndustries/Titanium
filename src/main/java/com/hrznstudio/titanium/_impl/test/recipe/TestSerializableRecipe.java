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
import net.minecraft.core.HolderLookup;
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

    static {
        new TestSerializableRecipe(Ingredient.of(new ItemStack(Items.OAK_SAPLING)), new ItemStack(Items.STICK, 3), Blocks.STONE);
        new TestSerializableRecipe(Ingredient.of(new ItemStack(Blocks.DIRT)), new ItemStack(Items.DIAMOND, 1), Blocks.DIRT);
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
        pick.setDamageValue(100);
        new TestSerializableRecipe(Ingredient.of(new ItemStack(Blocks.STONE)), pick, Blocks.DIRT);
    }

    public static final MapCodec<TestSerializableRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(i -> i.input),
        ItemStack.CODEC.fieldOf("output").forGetter(i -> i.output),
        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(i -> i.block)
    ).apply(in, TestSerializableRecipe::new));

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
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public ItemStack getToastSymbol() {
        return this.output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER.value();
    }

    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE.value();
    }

    public boolean isValid(ItemStack input, Block block) {
        return this.input.test(input) && this.block == block;
    }
}

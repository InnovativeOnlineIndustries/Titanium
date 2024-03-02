/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.recipe;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class TestSerializableRecipe extends SerializableRecipe {

    public static GenericSerializer<TestSerializableRecipe> SERIALIZER
            = new GenericSerializer<>(new ResourceLocation(Titanium.MODID, "test_serializer"), TestSerializableRecipe.class);
    public static final List<TestSerializableRecipe> RECIPES = new ArrayList<>();

    static {
        new TestSerializableRecipe(new ResourceLocation(Titanium.MODID, "saplings_to_sticks"), Ingredient.of(new ItemStack(Items.OAK_SAPLING)), new ItemStack(Items.STICK, 3), Blocks.STONE);
        new TestSerializableRecipe(new ResourceLocation(Titanium.MODID, "dirt_to_diamod"), Ingredient.of(new ItemStack(Blocks.DIRT)), new ItemStack(Items.DIAMOND, 1), Blocks.DIRT);
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
        pick.setDamageValue(100);
        new TestSerializableRecipe(new ResourceLocation(Titanium.MODID, "dirt_to_used"), Ingredient.of(new ItemStack(Blocks.STONE)), pick, Blocks.DIRT);
    }

    public Ingredient input;
    public ItemStack output;
    public Block block;

    public TestSerializableRecipe(ResourceLocation resourceLocation, Ingredient input, ItemStack output, Block block) {
        this(resourceLocation);
        this.input = input;
        this.output = output;
        this.block = block;
        RECIPES.add(this);
    }

    public TestSerializableRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return this.input.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public boolean isValid(ItemStack input, Block block) {
        return this.input.test(input) && this.block == block;
    }
}

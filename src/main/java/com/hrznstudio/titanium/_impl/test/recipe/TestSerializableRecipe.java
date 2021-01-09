/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl.test.recipe;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TestSerializableRecipe extends SerializableRecipe {

    public static GenericSerializer<TestSerializableRecipe> SERIALIZER
            = new GenericSerializer<>(new ResourceLocation(Titanium.MODID, "test_serializer"), TestSerializableRecipe.class);
    public static final List<TestSerializableRecipe> RECIPES = new ArrayList<>();

    static {
        new TestSerializableRecipe(new ResourceLocation(Titanium.MODID, "saplings_to_sticks"), Ingredient.fromStacks(new ItemStack(Items.OAK_SAPLING)), new ItemStack(Items.STICK, 3), Blocks.STONE);
        new TestSerializableRecipe(new ResourceLocation(Titanium.MODID, "dirt_to_diamod"), Ingredient.fromStacks(new ItemStack(Blocks.DIRT)), new ItemStack(Items.DIAMOND, 1), Blocks.DIRT);
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
        pick.setDamage(100);
        new TestSerializableRecipe(new ResourceLocation(Titanium.MODID, "dirt_to_used"), Ingredient.fromStacks(new ItemStack(Blocks.STONE)), pick, Blocks.DIRT);
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
    public boolean matches(IInventory inv, World worldIn) {
        return this.input.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public boolean isValid(ItemStack input, Block block) {
        return this.input.test(input) && this.block == block;
    }
}

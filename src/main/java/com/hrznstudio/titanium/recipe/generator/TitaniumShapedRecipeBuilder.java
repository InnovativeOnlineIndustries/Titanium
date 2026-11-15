/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;

import java.util.ArrayList;
import java.util.List;

public class TitaniumShapedRecipeBuilder extends ShapedRecipeBuilder {

    private ResourceLocation resourceLocation;
    private final List<ICondition> conditions;
    private boolean criterion;

    public TitaniumShapedRecipeBuilder(RecipeCategory recipeCategory, ItemLike resultIn, int countIn) {
        super(recipeCategory, resultIn, countIn);
        this.resourceLocation = BuiltInRegistries.ITEM.getKey(resultIn.asItem());
        this.conditions = new ArrayList<>();
        condition(new ItemExistsCondition(resourceLocation));
    }

    public static TitaniumShapedRecipeBuilder shapedRecipe(ItemLike resultIn) {
        return shapedRecipe(resultIn, 1);
    }

    /**
     * Creates a new builder for a shaped recipe.
     */
    public static TitaniumShapedRecipeBuilder shapedRecipe(ItemLike resultIn, int countIn) {
        return new TitaniumShapedRecipeBuilder(RecipeCategory.MISC, resultIn, countIn);
    }

    @Override
    public void save(RecipeOutput pRecipeOutput) {
        super.save(pRecipeOutput.withConditions(conditions.toArray(ICondition[]::new)));
    }

    @Override
    public ShapedRecipeBuilder define(Character symbol, TagKey<Item> tagIn) {
        if (!this.criterion) {
            this.criterion = true;
            unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tagIn).build()));
        }
        return super.define(symbol, tagIn);
    }

    @Override
    public ShapedRecipeBuilder define(Character symbol, Ingredient ingredientIn) {
        if (!this.criterion) {
            this.criterion = true;
            unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ingredientIn.getItems()[0].getItem()).build()));
        }
        return super.define(symbol, ingredientIn);
    }

    public TitaniumShapedRecipeBuilder setName(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public TitaniumShapedRecipeBuilder condition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }
}

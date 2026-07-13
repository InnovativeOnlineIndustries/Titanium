/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.RegisteredCondition;

import java.util.ArrayList;
import java.util.List;

public class TitaniumShapedRecipeBuilder {
    private final ShapedRecipeBuilder delegate;
    private final List<ICondition> conditions = new ArrayList<>();
    private Identifier resourceLocation;
    private boolean criterion;

    public TitaniumShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        this.delegate = ShapedRecipeBuilder.shaped(items, category, result, count);
        this.resourceLocation = BuiltInRegistries.ITEM.getKey(result.asItem());
        condition(new RegisteredCondition<>(ResourceKey.create(Registries.ITEM, resourceLocation)));
    }

    public static TitaniumShapedRecipeBuilder shapedRecipe(ItemLike result) {
        return shapedRecipe(result, 1);
    }

    public static TitaniumShapedRecipeBuilder shapedRecipe(ItemLike result, int count) {
        return new TitaniumShapedRecipeBuilder(BuiltInRegistries.ITEM, RecipeCategory.MISC, result, count);
    }

    public TitaniumShapedRecipeBuilder pattern(String row) {
        delegate.pattern(row);
        return this;
    }

    public TitaniumShapedRecipeBuilder define(Character symbol, ItemLike item) {
        return define(symbol, Ingredient.of(item));
    }

    public TitaniumShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        if (!criterion) {
            criterion = true;
            delegate.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, tag).build()));
        }
        delegate.define(symbol, tag);
        return this;
    }

    public TitaniumShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (!criterion) {
            criterion = true;
            ingredient.items().findFirst().ifPresent(item ->
                delegate.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(item.value())));
        }
        delegate.define(symbol, ingredient);
        return this;
    }

    public TitaniumShapedRecipeBuilder setName(Identifier resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public TitaniumShapedRecipeBuilder condition(ICondition condition) {
        conditions.add(condition);
        return this;
    }

    public void save(RecipeOutput output) {
        delegate.save(output.withConditions(conditions.toArray(ICondition[]::new)), ResourceKey.create(Registries.RECIPE, resourceLocation));
    }
}

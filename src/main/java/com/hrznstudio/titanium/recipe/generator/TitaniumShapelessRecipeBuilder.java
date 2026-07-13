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
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class TitaniumShapelessRecipeBuilder {
    private final ShapelessRecipeBuilder delegate;
    private boolean criterion;

    public TitaniumShapelessRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        this.delegate = ShapelessRecipeBuilder.shapeless(items, category, result, count);
    }

    public static TitaniumShapelessRecipeBuilder shapelessRecipe(ItemLike result) {
        return shapelessRecipe(result, 1);
    }

    public static TitaniumShapelessRecipeBuilder shapelessRecipe(ItemLike result, int count) {
        return new TitaniumShapelessRecipeBuilder(BuiltInRegistries.ITEM, RecipeCategory.MISC, result, count);
    }

    public TitaniumShapelessRecipeBuilder requires(ItemLike item) {
        return requires(Ingredient.of(item), 1);
    }

    public TitaniumShapelessRecipeBuilder requires(Ingredient ingredient, int quantity) {
        if (!criterion) {
            criterion = true;
            ingredient.items().findFirst().ifPresent(item ->
                delegate.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(item.value())));
        }
        delegate.requires(ingredient, quantity);
        return this;
    }

    public TitaniumShapelessRecipeBuilder requires(TagKey<Item> tag) {
        if (!criterion) {
            criterion = true;
            delegate.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, tag).build()));
        }
        delegate.requires(tag);
        return this;
    }

    public void save(RecipeOutput output) {
        delegate.save(output);
    }
}

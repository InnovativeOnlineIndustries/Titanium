/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class TitaniumShapelessRecipeBuilder extends ShapelessRecipeBuilder {

    private boolean criterion;

    public TitaniumShapelessRecipeBuilder(ItemLike resultIn, int countIn) {
        super(resultIn, countIn);
        this.criterion = false;
    }

    public static TitaniumShapelessRecipeBuilder shapelessRecipe(ItemLike resultIn) {
        return new TitaniumShapelessRecipeBuilder(resultIn, 1);
    }

    public static TitaniumShapelessRecipeBuilder shapelessRecipe(ItemLike resultIn, int countIn) {
        return new TitaniumShapelessRecipeBuilder(resultIn, countIn);
    }

    @Override
    public ShapelessRecipeBuilder requires(Ingredient ingredientIn, int quantity) {
        if (!this.criterion) {
            this.criterion = true;
            unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ingredientIn.getItems()[0].getItem()).build()));
        }
        return super.requires(ingredientIn, quantity);
    }

    @Override
    public ShapelessRecipeBuilder requires(Tag<Item> tagIn) {
        if (!this.criterion) {
            this.criterion = true;
            unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tagIn).build()));
        }
        return super.requires(tagIn);
    }
}

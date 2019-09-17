/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class TitaniumShapelessRecipeBuilder extends ShapelessRecipeBuilder {

    private boolean criterion;

    public TitaniumShapelessRecipeBuilder(IItemProvider resultIn, int countIn) {
        super(resultIn, countIn);
        this.criterion = false;
    }

    public static TitaniumShapelessRecipeBuilder shapelessRecipe(IItemProvider resultIn) {
        return new TitaniumShapelessRecipeBuilder(resultIn, 1);
    }

    public static TitaniumShapelessRecipeBuilder shapelessRecipe(IItemProvider resultIn, int countIn) {
        return new TitaniumShapelessRecipeBuilder(resultIn, countIn);
    }

    @Override
    public ShapelessRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        if (!this.criterion) {
            this.criterion = true;
            addCriterion("has_item", new InventoryChangeTrigger.Instance(MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, new ItemPredicate[]{ItemPredicate.Builder.create().item(ingredientIn.getMatchingStacks()[0].getItem()).build()}));
        }
        return super.addIngredient(ingredientIn, quantity);
    }
}

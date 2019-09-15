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
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class TitaniumShapedRecipeBuilder extends ShapedRecipeBuilder implements IConditionBuilder {

    private ResourceLocation resourceLocation;
    private ConditionalRecipe.Builder conditional;
    private boolean build;
    private boolean criterion;

    public TitaniumShapedRecipeBuilder(IItemProvider resultIn, int countIn) {
        super(resultIn, countIn);
        this.resourceLocation = resultIn.asItem().getRegistryName();
        this.build = false;
        this.conditional = ConditionalRecipe.builder().addCondition(
                and(
                        itemExists(resourceLocation.getNamespace(), resourceLocation.getPath())
                ));
    }

    public static TitaniumShapedRecipeBuilder shapedRecipe(IItemProvider resultIn) {
        return shapedRecipe(resultIn, 1);
    }

    /**
     * Creates a new builder for a shaped recipe.
     */
    public static TitaniumShapedRecipeBuilder shapedRecipe(IItemProvider resultIn, int countIn) {
        return new TitaniumShapedRecipeBuilder(resultIn, countIn);
    }

    @Override
    public void build(Consumer<IFinishedRecipe> consumerIn) {
        if (!this.build) {
            this.build = true;
            this.conditional.addRecipe(this::build).build(consumerIn, resourceLocation);
        } else {
            this.build(consumerIn, resourceLocation);
        }
    }

    @Override
    public ShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn) {
        if (!this.criterion) {
            this.criterion = true;
            addCriterion("has_item", new InventoryChangeTrigger.Instance(MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, new ItemPredicate[]{ItemPredicate.Builder.create().item(ingredientIn.getMatchingStacks()[0].getItem()).build()}));
        }
        return super.key(symbol, ingredientIn);
    }

    public TitaniumShapedRecipeBuilder setName(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public ConditionalRecipe.Builder getConditional() {
        return conditional;
    }

    public TitaniumShapedRecipeBuilder setConditional(ConditionalRecipe.Builder conditional) {
        this.conditional = conditional;
        return this;
    }
}

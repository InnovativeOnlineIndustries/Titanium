/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class TitaniumShapedRecipeBuilder extends ShapedRecipeBuilder implements IConditionBuilder {

    private ResourceLocation resourceLocation;
    private ConditionalRecipe.Builder conditional;
    private boolean build;
    private boolean criterion;

    public TitaniumShapedRecipeBuilder(ItemLike resultIn, int countIn) {
        super(resultIn, countIn);
        this.resourceLocation = resultIn.asItem().getRegistryName();
        this.build = false;
        this.conditional = ConditionalRecipe.builder().addCondition(
                and(
                        itemExists(resourceLocation.getNamespace(), resourceLocation.getPath())
                ));
    }

    public static TitaniumShapedRecipeBuilder shapedRecipe(ItemLike resultIn) {
        return shapedRecipe(resultIn, 1);
    }

    /**
     * Creates a new builder for a shaped recipe.
     */
    public static TitaniumShapedRecipeBuilder shapedRecipe(ItemLike resultIn, int countIn) {
        return new TitaniumShapedRecipeBuilder(resultIn, countIn);
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumerIn) {
        if (!this.build) {
            this.build = true;
            this.conditional.addRecipe(this::save).build(consumerIn, resourceLocation);
        } else {
            this.save(consumerIn, resourceLocation);
        }
    }

    @Override
    public ShapedRecipeBuilder define(Character symbol, Tag<Item> tagIn) {
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

    public ConditionalRecipe.Builder getConditional() {
        return conditional;
    }

    public TitaniumShapedRecipeBuilder setConditional(ConditionalRecipe.Builder conditional) {
        this.conditional = conditional;
        return this;
    }
}

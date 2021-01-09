/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.shapelessenchant;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Map;

public class ShapelessEnchantSerializer extends ShapelessRecipe.Serializer {
    @Nonnull
    public ShapelessRecipe read(@Nonnull ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (ingredients.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 9);
        } else {
            JsonObject jsonObject = JSONUtils.getJsonObject(json, "result");
            ItemStack itemstack = ShapedRecipe.deserializeItem(jsonObject);
            if (jsonObject.has("enchantments")) {
                JsonElement enchantments = jsonObject.get("enchantments");
                Map<Enchantment, Integer> enchantmentLevelMap = Maps.newHashMap();
                if (enchantments.isJsonArray()) {
                    for (JsonElement jsonElement: enchantments.getAsJsonArray()) {
                        if (jsonElement.isJsonObject()) {
                            Pair<Enchantment, Integer> enchantmentLevelPair = parseJson(jsonElement.getAsJsonObject());
                            enchantmentLevelMap.put(enchantmentLevelPair.getKey(), enchantmentLevelPair.getValue());
                        }
                    }
                } else if (enchantments.isJsonObject()) {
                    Pair<Enchantment, Integer> enchantmentLevelPair = parseJson(enchantments.getAsJsonObject());
                    enchantmentLevelMap.put(enchantmentLevelPair.getKey(), enchantmentLevelPair.getValue());
                }
                EnchantmentHelper.setEnchantments(enchantmentLevelMap, itemstack);
            } else {
                throw new JsonParseException("No String or Array found for enchantments");
            }
            return new ShapelessRecipe(recipeId, s, itemstack, ingredients);
        }
    }

    private static Pair<Enchantment, Integer> parseJson(JsonObject jsonObject) {
        String name = JSONUtils.getString(jsonObject, "name");
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(name));
        if (enchantment == null) {
            throw new JsonParseException("Failed to find enchantment named: " + name);
        }
        return Pair.of(enchantment, JSONUtils.getInt(jsonObject, "level", 1));
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for(int i = 0; i < ingredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
            if (!ingredient.hasNoMatchingItems()) {
                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }
}

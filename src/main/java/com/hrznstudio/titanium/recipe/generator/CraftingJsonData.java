/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class CraftingJsonData implements IJsonFile {

    private String type;
    private String[] pattern;
    private PatternKey key;
    private IIngredient[] ingredients;
    private IIngredient result;

    private CraftingJsonData(String type, String[] pattern, PatternKey key, IIngredient result) {
        this.type = type;
        this.pattern = pattern;
        this.key = key;
        this.result = result;
    }

    private CraftingJsonData(String type, String[] pattern, IIngredient[] ingredients, IIngredient result) {
        this.type = type;
        this.pattern = pattern;
        this.ingredients = ingredients;
        this.result = result;
    }

    public static CraftingJsonData ofShaped(ItemStack stack, String[] pattern, Object... keyPairs) {
        IIngredient result = IIngredient.ItemStackIngredient.of(stack);
        HashMap<Character, Character> charMapping = new HashMap<>();
        char letter = 'A';
        for (String patternLine : pattern) {
            for (char c : patternLine.toCharArray()) {
                if (c == ' ') continue;
                if (!charMapping.containsKey(c)) {
                    charMapping.put(c, letter);
                    ++letter;
                }
            }
        }
        charMapping.forEach((key, value) -> {
            for (int i = 0; i < pattern.length; i++) {
                pattern[i] = pattern[i].replace(key, value);
            }
        });
        PatternKey patternKey = new PatternKey();
        for (int i = 0; i < keyPairs.length; i = i + 2) {
            char key = (char) keyPairs[i];
            IIngredient value = (IIngredient) keyPairs[i + 1];
            patternKey.set(charMapping.get(key) - 'A', value);
        }
        return new CraftingJsonData("minecraft:crafting_shaped", pattern, patternKey, result);
    }

    public static CraftingJsonData ofShapeless(ItemStack stack, IIngredient... keys) {
        IIngredient result = IIngredient.ItemStackIngredient.of(stack);
        return new CraftingJsonData("minecraft:crafting_shapeless", null, keys, result);
    }

    @Override
    public String getRecipeKey() {
        return result.getKey();
    }

    private static class PatternKey {
        public IIngredient A, B, C, D, E, F, G, H, I;

        public void set(int i, IIngredient ingredient) {
            switch (i) {
                case 0:
                    A = ingredient;
                    break;
                case 1:
                    B = ingredient;
                    break;
                case 2:
                    C = ingredient;
                    break;
                case 3:
                    D = ingredient;
                    break;
                case 4:
                    E = ingredient;
                    break;
                case 5:
                    F = ingredient;
                    break;
                case 6:
                    G = ingredient;
                    break;
                case 7:
                    H = ingredient;
                    break;
                case 8:
                    I = ingredient;
                    break;
            }
        }
    }
}

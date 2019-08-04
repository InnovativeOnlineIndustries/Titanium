/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FurnaceJsonData implements IJsonFile {

    public String type = "minecraft:smelting";
    public IIngredient ingredient;
    public String result;
    public double experience;
    public int cookingtime;

    private FurnaceJsonData(IIngredient ingredient, String result, double experience, int cookingtime) {
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.cookingtime = cookingtime;
    }

    public static FurnaceJsonData of(IIngredient iIngredient, ItemStack result, double experience, int cookingtime) {
        return new FurnaceJsonData(iIngredient, result.getItem().getRegistryName().toString(), experience, cookingtime);
    }

    @Override
    public String getRecipeKey() {
        return new ResourceLocation(result).getPath();
    }
}

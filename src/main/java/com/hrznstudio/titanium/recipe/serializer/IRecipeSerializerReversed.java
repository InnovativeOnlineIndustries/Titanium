package com.hrznstudio.titanium.recipe.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeSerializerReversed<T extends IRecipe<?>> {

    public JsonObject write(T recipe);

}

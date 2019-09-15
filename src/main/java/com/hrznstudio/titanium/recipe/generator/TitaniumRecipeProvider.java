package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

public abstract class TitaniumRecipeProvider extends RecipeProvider {

    public TitaniumRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        register(consumer);
    }

    public abstract void register(Consumer<IFinishedRecipe> consumer);


    @Override
    public String getName() {
        return "Titanium Recipe";
    }
}

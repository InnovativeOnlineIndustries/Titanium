package com.hrznstudio.titanium.api;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public interface IRecipeProvider {

    void registerRecipe(Consumer<IFinishedRecipe> consumer);

}

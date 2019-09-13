package com.hrznstudio.titanium.recipe.generator;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class TitaniumShapedRecipeBuilder extends ShapedRecipeBuilder {

    private ResourceLocation resourceLocation;

    public TitaniumShapedRecipeBuilder(IItemProvider resultIn, int countIn) {
        super(resultIn, countIn);
        this.resourceLocation = resultIn.asItem().getRegistryName();
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
        this.build(consumerIn, resourceLocation);
    }

    public TitaniumShapedRecipeBuilder setName(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

}

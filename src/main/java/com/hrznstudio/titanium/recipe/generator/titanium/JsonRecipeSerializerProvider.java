package com.hrznstudio.titanium.recipe.generator.titanium;

import com.hrznstudio.titanium._impl.test.recipe.TestSerializableRecipe;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;

import java.util.Map;

public class JsonRecipeSerializerProvider extends TitaniumSerializableProvider {

    public JsonRecipeSerializerProvider(DataGenerator generatorIn, String modid) {
        super(generatorIn, modid);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        TestSerializableRecipe.RECIPES.forEach(testSerializableRecipe -> serializables.put(testSerializableRecipe, testSerializableRecipe));
    }
}

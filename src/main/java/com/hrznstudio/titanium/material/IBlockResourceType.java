package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;

public interface IBlockResourceType extends IJSONGenerator {

    int getColor(ResourceMaterial material, int tintIndex);

}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;

public interface IBlockResourceType extends IJSONGenerator {

    int getColor(ResourceMaterial material, int tintIndex);

}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.serializer;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.Recipe;

public interface IRecipeSerializerReversed<T extends Recipe<?>> {
    JsonObject write(T recipe);
}

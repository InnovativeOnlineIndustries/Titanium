/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.serializer;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class SerializableRecipe implements IRecipe<IInventory>, IJsonFile, IJSONGenerator {

    public String type = getSerializer().getRegistryName().toString();
    private transient ResourceLocation resourceLocation;

    public SerializableRecipe(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public ResourceLocation getId() {
        return resourceLocation;
    }

    @Override
    public String getRecipeKey() {
        return resourceLocation.getPath();
    }

    @Nullable
    @Override
    public String getRecipeSubfolder() {
        return new ResourceLocation(type).getPath();
    }

    @Override
    public abstract GenericSerializer<? extends SerializableRecipe> getSerializer();

    @Override
    public JsonObject generate() {
        return ((GenericSerializer<SerializableRecipe>) getSerializer()).write(this);
    }
}

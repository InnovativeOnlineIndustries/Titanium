/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.serializer;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

public abstract class SerializableRecipe implements Recipe<Container>, IJsonFile, IJSONGenerator {

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

    @Nullable
    public Pair<ICondition, IConditionSerializer> getOutputCondition(){
        return null;
    }

}

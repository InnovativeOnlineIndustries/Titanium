/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.condition;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class ContentExistsConditionSerializer implements IConditionSerializer<ContentExistsCondition> {
    @Override
    public void write(JsonObject json, ContentExistsCondition value) {
        json.addProperty("registry", value.getForgeRegistry().getRegistryName().toString());
        json.addProperty("name", value.getContentName().toString());
    }

    @Override
    public ContentExistsCondition read(JsonObject json) {
        String registryName = GsonHelper.getAsString(json, "registry");
        IForgeRegistry<?> forgeRegistry = RegistryManager.ACTIVE.getRegistry(new ResourceLocation(registryName));
        if (forgeRegistry == null) {
            throw new JsonParseException("Didn't Find Registry for registry: " + registryName);
        }
        return new ContentExistsCondition(forgeRegistry, new ResourceLocation(GsonHelper.getAsString(json, "name")));
    }

    @Override
    public ResourceLocation getID() {
        return ContentExistsCondition.NAME;
    }
}

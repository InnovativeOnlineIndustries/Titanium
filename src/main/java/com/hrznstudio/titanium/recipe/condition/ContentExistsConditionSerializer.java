package com.hrznstudio.titanium.recipe.condition;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
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
        String registryName = JSONUtils.getString(json, "registry");
        IForgeRegistry<?> forgeRegistry = RegistryManager.ACTIVE.getRegistry(new ResourceLocation(registryName));
        if (forgeRegistry == null) {
            throw new JsonParseException("Didn't Find Registry for registry: " + registryName);
        }
        return new ContentExistsCondition(forgeRegistry, new ResourceLocation(JSONUtils.getString(json, "name")));
    }

    @Override
    public ResourceLocation getID() {
        return ContentExistsCondition.NAME;
    }
}

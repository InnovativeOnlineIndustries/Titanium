/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material.advancedtype;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.material.IAdvancedResourceType;
import com.hrznstudio.titanium.material.ResourceMaterial;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum ItemAdvancedResourceType implements IAdvancedResourceType {
    SIMPLE((material, integer) -> material.getColor(), (type) -> {
        JsonObject object = new JsonObject();
        object.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", new ResourceLocation(Titanium.MODID, "items/resource/" + type.getSerializedName()).toString()); //getNAme
        object.add("textures", textures);
        return object;
    });

    private final BiFunction<ResourceMaterial, Integer, Integer> colorFunction;
    private final Function<IResourceType, JsonObject> jsonObjectSupplier;

    ItemAdvancedResourceType(BiFunction<ResourceMaterial, Integer, Integer> colorFunction, Function<IResourceType, JsonObject> jsonObjectSupplier) {
        this.colorFunction = colorFunction;
        this.jsonObjectSupplier = jsonObjectSupplier;
    }

    @Override
    public int getColor(ResourceMaterial material, int tintIndex) {
        return colorFunction.apply(material, tintIndex);
    }

    @Override
    public JsonObject generate(IResourceType type) {
        return jsonObjectSupplier.apply(type);
    }
}

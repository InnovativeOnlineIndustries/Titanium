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
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum BlockAdvancedResourceType implements IAdvancedResourceType {
    METAL_BLOCK((material1, integer) -> material1.getColor(), (type) -> {
        JsonObject object = new JsonObject();
        object.addProperty("parent", "block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", new ResourceLocation(Titanium.MODID, "blocks/resource/metal_block").toString());
        object.add("textures", textures);
        return object;
    }),
    ORE((material1, integer) -> integer == 0 ? material1.getColor() : 1, (type) -> {
        JsonObject object = new JsonObject();
        object.addProperty("parent", Titanium.MODID + ":block/ore");
        JsonObject textures = new JsonObject();
        textures.addProperty("ore", new ResourceLocation(Titanium.MODID, "blocks/resource/ore_overlay").toString());
        object.add("textures", textures);
        return object;
    }),
    NETHER_ORE((material1, integer) -> integer == 0 ? material1.getColor() : 1, (type) -> {
        JsonObject object = new JsonObject();
        object.addProperty("parent", Titanium.MODID + ":block/ore");
        JsonObject textures = new JsonObject();
        textures.addProperty("ore", new ResourceLocation(Titanium.MODID, "blocks/resource/ore_overlay").toString());
        textures.addProperty("particle", "blocks/netherrack");
        textures.addProperty("texture", "blocks/netherrack");
        object.add("textures", textures);
        return object;
    }),
    GEM_BLOCK((material1, integer) -> material1.getColor(), (type) -> {
        JsonObject object = new JsonObject();
        object.addProperty("parent", "block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", new ResourceLocation(Titanium.MODID, "blocks/resource/gem_block").toString());
        object.add("textures", textures);
        return object;
    });

    private final BiFunction<ResourceMaterial, Integer, Integer> colorFunction;
    private final Function<IResourceType, JsonObject> jsonObjectSupplier;

    BlockAdvancedResourceType(BiFunction<ResourceMaterial, Integer, Integer> colorFunction, Function<IResourceType, JsonObject> jsonObjectSupplier) {
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

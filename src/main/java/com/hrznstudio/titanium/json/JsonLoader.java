/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hrznstudio.titanium.json.jsondirector.IJsonDirector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.Tuple;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class JsonLoader<T> extends SimpleJsonResourceReloadListener {
    private final IJsonDirector<T> director;
    private final String type;
    private final Logger logger;
    private final IJsonProvider<T> jsonProvider;

    public JsonLoader(String type, Logger logger, IJsonDirector<T> director, IJsonProvider<T> jsonProvider) {
        super(new Gson(), type);
        this.type = type;
        this.logger = logger;
        this.director = director;
        this.jsonProvider = jsonProvider;
    }

    private ResourceLocation transformRL(ResourceLocation resource) {
        return new ResourceLocation(resource.getNamespace(), resource.getPath().replace(type + "/", ""));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> ts, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        director.clear();
        ts.entrySet()
            .parallelStream()
            .map(entry -> new Tuple<>(entry.getKey(), jsonProvider.provide(entry.getKey(), entry.getValue().getAsJsonObject())))
            .forEach(tuple -> director.put(tuple.getA(), tuple.getB()));
        logger.info("Loaded " + ts.size() + " " + type);
    }
}

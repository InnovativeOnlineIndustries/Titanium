/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hrznstudio.titanium.json.jsondirector.IJsonDirector;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Tuple;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class JsonLoader<T> extends SimplePreparableReloadListener<Map<Identifier, JsonElement>> {
    private final IJsonDirector<T> director;
    private final String type;
    private final Logger logger;
    private final IJsonProvider<T> jsonProvider;

    public JsonLoader(String type, Logger logger, IJsonDirector<T> director, IJsonProvider<T> jsonProvider) {
        this.type = type;
        this.logger = logger;
        this.director = director;
        this.jsonProvider = jsonProvider;
    }

    @Override
    protected Map<Identifier, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        FileToIdConverter converter = FileToIdConverter.json(type);
        Map<Identifier, JsonElement> result = new HashMap<>();
        converter.listMatchingResources(resourceManager).forEach((file, resource) -> {
            try (Reader reader = resource.openAsReader()) {
                result.put(converter.fileToId(file), JsonParser.parseReader(reader));
            } catch (IOException | RuntimeException exception) {
                logger.error("Couldn't parse {} JSON {}", type, file, exception);
            }
        });
        return result;
    }

    private Identifier transformRL(Identifier resource) {
        return Identifier.fromNamespaceAndPath(resource.getNamespace(), resource.getPath().replace(type + "/", ""));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> ts, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        director.clear();
        ts.entrySet()
            .parallelStream()
            .map(entry -> new Tuple<>(entry.getKey(), jsonProvider.provide(entry.getKey(), entry.getValue().getAsJsonObject())))
            .forEach(tuple -> director.put(tuple.getA(), tuple.getB()));
        logger.info("Loaded " + ts.size() + " " + type);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class TitaniumModelLoader implements IModelLoader {
    private final Map<ResourceLocation, UnbakedModel> MODEL_MAP = new HashMap<>();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        MODEL_MAP.clear();
        MinecraftForge.EVENT_BUS.post(new TitaniumModelEvent(this));
    }

    @Override
    public IModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return null;
    }

    public static class TitaniumModelEvent extends Event {
        private final TitaniumModelLoader loader;

        public TitaniumModelEvent(TitaniumModelLoader loader) {
            this.loader = loader;
        }

        public void register(ResourceLocation resourceLocation, UnbakedModel model) {
            loader.MODEL_MAP.put(resourceLocation, model);
        }

        public void register(String location, UnbakedModel model) {
            register(new ResourceLocation(location), model);
        }

        public void register(String domain, String location, UnbakedModel model) {
            register(new ResourceLocation(domain, location), model);
        }
    }
}

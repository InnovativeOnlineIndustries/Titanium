/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client;

import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class TitaniumModelLoader implements ICustomModelLoader {
    private final Map<ResourceLocation, IUnbakedModel> MODEL_MAP = new HashMap<>();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return MODEL_MAP.containsKey(modelLocation);
    }

    @Override
    @Nonnull
    public IUnbakedModel loadModel(ResourceLocation modelLocation) {
        return MODEL_MAP.get(modelLocation);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        MODEL_MAP.clear();
        MinecraftForge.EVENT_BUS.post(new TitaniumModelEvent(this));
    }

    public static class TitaniumModelEvent extends Event {
        private final TitaniumModelLoader loader;

        public TitaniumModelEvent(TitaniumModelLoader loader) {
            this.loader = loader;
        }

        public void register(ResourceLocation resourceLocation, IUnbakedModel model) {
            loader.MODEL_MAP.put(resourceLocation, model);
        }

        public void register(String location, IUnbakedModel model) {
            register(new ResourceLocation(location), model);
        }

        public void register(String domain, String location, IUnbakedModel model) {
            register(new ResourceLocation(domain, location), model);
        }
    }
}
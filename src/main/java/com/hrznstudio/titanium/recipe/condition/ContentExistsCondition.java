/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.condition;

import com.hrznstudio.titanium.Titanium;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.conditions.ICondition;


public record ContentExistsCondition<T>(HolderGetter<T> registry, ResourceKey<T> contentName) implements ICondition {
    public static final MapCodec<ContentExistsCondition<?>> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
        ResourceLocation.CODEC.fieldOf("registry").forGetter(ce -> ce.contentName().registry()),
        ResourceLocation.CODEC.fieldOf("name").forGetter(ce -> ce.contentName().location()),
        ExtraCodecs.retrieveContext(ops -> ops instanceof RegistryOps<?> rops ? DataResult.success(rops) : DataResult.<RegistryOps<?>>error(() -> "Not a registry ops")).forGetter(ce -> null)
    ).apply(in, (reg, name, ops) -> {
        final ResourceKey regKey = ResourceKey.createRegistryKey(reg);
        return new ContentExistsCondition((HolderGetter) ops.getter(regKey).orElseThrow(), ResourceKey.create(regKey, name));
    }));
    public static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(Titanium.MODID, "content_exists");

    @Override
    public boolean test(IContext context) {
        return registry.get(contentName).isPresent();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}

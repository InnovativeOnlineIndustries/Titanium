/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.serializer;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public final class CodecRecipeSerializer {
    private CodecRecipeSerializer() {
    }

    public static <T extends Recipe<?>> RecipeSerializer<T> create(Class<T> recipeClass, Supplier<RecipeType<?>> recipeTypeSupplier, MapCodec<T> codec) {
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec.codec());
        return new RecipeSerializer<>(codec, streamCodec);
    }
}


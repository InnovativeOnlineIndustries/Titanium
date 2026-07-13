/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.compat.almostunified;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AlmostUnifiedAdapter {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("almostunified");
    }

    @Nullable
    public static Item getPreferredItemForTag(TagKey<Item> tagKey) {
        if (isLoaded()) {
            return Adapter.getPreferredItemForTag(tagKey);
        }

        return null;
    }

    private static class Adapter {
        private static final String API_CLASS = "com.almostreliable.unified.api.AlmostUnified";

        @Nullable
        private static Item getPreferredItemForTag(TagKey<Item> tag) {
            try {
                Class<?> apiClass = Class.forName(API_CLASS);
                Field instanceField = apiClass.getField("INSTANCE");
                Method targetMethod = apiClass.getMethod("getTagTargetItem", TagKey.class);
                return (Item) targetMethod.invoke(instanceField.get(null), tag);
            } catch (ReflectiveOperationException | LinkageError ignored) {
                return null;
            }
        }
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.json.jsonprovider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hrznstudio.titanium.json.IJsonProvider;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class GenericSerializerJsonProvider<T> implements IJsonProvider<T> {

    private final Class<T> tClass;

    public GenericSerializerJsonProvider(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public T provide(ResourceLocation targetID, JsonObject jsonObject) throws JsonParseException {
        try {
            T target = tClass.getConstructor(ResourceLocation.class).newInstance(targetID);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (fieldExists(entry.getKey()) && JSONSerializableDataHandler.acceptField(tClass.getField(entry.getKey()), tClass.getField(entry.getKey()).getType())) {
                    tClass.getField(entry.getKey()).set(target, JSONSerializableDataHandler.read(tClass.getField(entry.getKey()).getType(), entry.getValue()));
                }
            }
            return target;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            throw new JsonParseException(e);
        }
    }

    private boolean fieldExists(String field) {
        for (Field tclassField : tClass.getFields()) {
            if (tclassField.getName().equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }
}

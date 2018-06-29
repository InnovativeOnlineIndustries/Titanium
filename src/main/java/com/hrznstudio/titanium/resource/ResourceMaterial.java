/*
 * This file is part of Electrodynamics
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.resource;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.util.ArrayUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ResourceMaterial {
    public String materialName;
    public int meta;

    private Map<Attribute, Float> attributeMap = new HashMap<>();
    private List<ResourceType> types = new ArrayList<>();
    private Function<ResourceType, ModelResourceLocation> modelFunction;

    public ResourceMaterial(Function<ResourceType, ModelResourceLocation> modelFunction) {
        this.modelFunction = modelFunction;
    }

    public String getLocalizedName() {
        return I18n.translateToLocal("item.edxcore.material." + materialName + ".name");
    }

    public Function<ResourceType, ModelResourceLocation> getModelFunction() {
        return modelFunction;
    }

    public boolean hasType(ResourceType resource) {
        return types.contains(resource);
    }

    public ResourceMaterial withAttribute(Attribute attribute, float value) {
        if (!hasAttribute(attribute)) {
            this.attributeMap.put(attribute, value);
        } else {
            float original = getValue(attribute);
            this.attributeMap.put(attribute, original + value);
        }
        return this;
    }

    public ResourceMaterial withTypes(List<ResourceType> types) {
        types.forEach(this::withType);
        return this;
    }
    public ResourceMaterial withTypes(ResourceType... types) {
        ArrayUtil.forEach(this::withType, types);
        return this;
    }

    public List<ResourceType> getTypes() {
        return ImmutableList.copyOf(types);
    }

    public ResourceMaterial withType(ResourceType type) {
        types.add(type);
        return this;
    }

    public boolean hasAttribute(Attribute attribute) {
        return attributeMap.containsKey(attribute);
    }

    public float getValue(Attribute attribute) {
        return attributeMap.getOrDefault(attribute, 0f);
    }
}
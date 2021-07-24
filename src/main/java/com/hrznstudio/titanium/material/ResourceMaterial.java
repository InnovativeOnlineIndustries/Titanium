/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.api.material.IResourceType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ResourceMaterial {

    private String type;
    private HashMap<String, IResourceType> generatorTypes;
    private HashMap<String, ForgeRegistryEntry> generatorOverrides;
    private HashMap<String, ForgeRegistryEntry> generated;
    private HashMap<String, ResourceTypeProperties> typeProperties;
    private int color;

    ResourceMaterial(String type) {
        this.type = type;
        this.generatorTypes = new HashMap<>();
        this.generatorOverrides = new HashMap<>();
        this.generated = new HashMap<>();
        this.typeProperties = new HashMap<>();
    }

    public String getMaterialType() {
        return type;
    }

    public ResourceMaterial add(IResourceType type) {
        generatorTypes.computeIfAbsent(type.getTag(), s -> type);
        return this;
    }

    public ResourceMaterial addAll(IResourceType... type) {
        for (IResourceType iResourceType : type) {
            add(iResourceType);
        }
        return this;
    }

    public ResourceMaterial withOverride(IResourceType type, ForgeRegistryEntry entry) {
        generatorOverrides.put(type.getTag(), entry);
        return this;
    }

    public ResourceMaterial withProperties(IResourceType type, ResourceTypeProperties properties) {
        typeProperties.computeIfAbsent(type.getSerializedName(), s -> properties); //getName
        return this;
    }

    public int getColor() {
        return color;
    }

    public ResourceMaterial setColor(int color) {
        this.color = color;
        return this;
    }

    public Map<String, IResourceType> getGeneratorTypes() {
        return generatorTypes;
    }

    public Map<String, ForgeRegistryEntry> getGeneratorOverrides() {
        return generatorOverrides;
    }

    public Map<String, ForgeRegistryEntry> getGenerated() {
        return generated;
    }

    @Nullable
    public ForgeRegistryEntry generate(IResourceType type) {
        if (generatorOverrides.containsKey(type.getTag())) {
            ForgeRegistryEntry entry = generatorOverrides.get(type.getTag());
            generated.put(type.getTag(), entry);
            ResourceRegistry.injectField(this, type, entry);
            return null;
        }
        ForgeRegistryEntry entry = type.getInstanceFactory(this, typeProperties.get(type.getSerializedName())).create(); //getName
        generated.put(type.getTag(), entry);
        ResourceRegistry.injectField(this, type, entry);
        return entry;
    }

    public Component getTextComponent() {
        return new TranslatableComponent(String.format("resource.titanium.material.%s", type));
    }
}

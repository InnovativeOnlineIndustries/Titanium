package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.api.material.IResourceType;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ResourceMaterial {

    private String type;
    private HashMap<String, IResourceType> generatorTypes;
    private HashMap<String, ForgeRegistryEntry> generatorOverrides;
    private HashMap<String, ForgeRegistryEntry> generated;
    private int color;

    ResourceMaterial(String type) {
        this.type = type;
        this.generatorTypes = new HashMap<>();
        this.generatorOverrides = new HashMap<>();
        this.generated = new HashMap<>();
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

    public ResourceMaterial addOverride(IResourceType type, ForgeRegistryEntry entry) {
        generatorOverrides.put(type.getTag(), entry);
        return this;
    }

    public int getColor() {
        return color;
    }

    public ResourceMaterial setColor(int color) {
        this.color = color;
        return this;
    }

    public HashMap<String, IResourceType> getGeneratorTypes() {
        return generatorTypes;
    }

    public HashMap<String, ForgeRegistryEntry> getGeneratorOverrides() {
        return generatorOverrides;
    }

    public HashMap<String, ForgeRegistryEntry> getGenerated() {
        return generated;
    }

    @Nullable
    public ForgeRegistryEntry generate(IResourceType type) {
        if (generatorOverrides.containsKey(type.getTag())) {
            generated.put(type.getTag(), generatorOverrides.get(type.getTag()));
            return null;
        }
        ForgeRegistryEntry entry = type.getInstanceFactory(this).create();
        generated.put(type.getTag(), entry);
        return entry;
    }
}

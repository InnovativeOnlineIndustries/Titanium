package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.api.material.IResourceType;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;

public class ResourceMaterial {

    private Type type;
    private HashMap<String, IResourceType> generatorTypes;
    private HashMap<String, ForgeRegistryEntry> generatorOverrides;

    ResourceMaterial(Type type) {
        this.type = type;
        this.generatorTypes = new HashMap<>();
        this.generatorOverrides = new HashMap<>();
    }

    public Type getMaterialType() {
        return type;
    }

    public ResourceMaterial add(IResourceType type) {
        generatorTypes.computeIfAbsent(type.getTag(), s -> type);
        return this;
    }

    public ResourceMaterial addOverride(IResourceType type, ForgeRegistryEntry entry) {
        generatorOverrides.put(type.getTag(), entry);
        return this;
    }

    public interface Type {

        String getType();

    }
}

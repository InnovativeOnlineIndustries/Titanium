package com.hrznstudio.titanium.material;

import java.util.HashMap;

public class ResourceRegistry {

    private static HashMap<String, ResourceMaterial> MATERIALS = new HashMap<>();

    public static ResourceMaterial getOrCreate(ResourceMaterial.Type type) {
        return MATERIALS.computeIfAbsent(type.getType(), s -> new ResourceMaterial(type));
    }

}

/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.api.resource;

import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ResourceRegistry {
    public static final Logger LOGGER = LogManager.getLogger("Titanium - Resource Registry");
    private static HashMap<String, ResourceMaterial> nameToMaterial = new HashMap<>();
    private static int nextID = -1;

    private static boolean errorLog = false;

    public static ResourceMaterial addMaterial(String name, ResourceMaterial material) {
        material.materialName = name;
        if (nameToMaterial.containsKey(name)) {
            ResourceMaterial original = nameToMaterial.get(name);
            original.withTypes(material.getTypes());
            original.getModelFunctions().addAll(material.getModelFunctions());
            return original;
        } else {
            nameToMaterial.put(name, material);
        }
        return material;
    }

    public static Optional<ResourceMaterial> getMaterial(String id) {
        return Optional.ofNullable(id != null ? nameToMaterial.get(id) : null);
    }

    public static List<ResourceMaterial> getMaterials() {
        return ImmutableList.copyOf(nameToMaterial.values());
    }
}

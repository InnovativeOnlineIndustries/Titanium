/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.resource;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ResourceRegistry {
    private static HashMap<String, ResourceMaterial> nameToMaterial = new HashMap<>();
    private static HashMap<String, Integer> nameToMeta = new HashMap<>();
    private static HashMap<Integer, String> metaToName = new HashMap<>();
    public static final Logger LOGGER = LogManager.getLogger("Titanium - Resource Registry");
    private static int nextID = -1;

    private static boolean errorLog = false;

    public static ResourceMaterial addMaterial(String name, ResourceMaterial material) {
        material.materialName = name;
        //TODO: Replace with incompatibility manager
        if (!errorLog && Loader.isModLoaded("techreborn")) {
            errorLog = true;
            LOGGER.error("\n" +
                    "***************************************************************************" +
                    "* WARNING!!!" +
                    "* Materials have been registered while tech reborn is loaded\n" +
                    "* this is not compatible and most likely will break. DO NOT report this to Matter Overdrive\n" +
                    "***************************************************************************"
            );
        }
        if (nameToMeta.containsKey(name)) {
            ResourceMaterial original = nameToMaterial.get(name);
            original.withTypes(material.getTypes());
            return original;
        } else {
            nameToMaterial.put(name, material);
            while (nextID < 32000) {
                nextID++;
                if (metaToName.get(nextID) == null) {
                    metaToName.put(nextID, name);
                    nameToMeta.put(name, nextID);
                    material.meta = nextID;
                    break;
                }
            }

            if (nextID >= 32000) {
                throw new RuntimeException("Fuck.");
            }
        }
        return material;
    }

    public static Optional<ResourceMaterial> getMaterial(int meta) {
        return getMaterial(getName(meta));
    }

    public static Optional<ResourceMaterial> getMaterial(String name) {
        return Optional.ofNullable(name != null ? nameToMaterial.get(name) : null);
    }

    public static String getName(int meta) {
        return metaToName.get(meta);
    }

    public static int getMeta(String name) {
        return nameToMeta.get(name);
    }

    public static List<ResourceMaterial> getMaterials() {
        return ImmutableList.copyOf(nameToMaterial.values());
    }
}

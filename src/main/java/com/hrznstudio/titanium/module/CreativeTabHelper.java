/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreativeTabHelper {

    private HashMap<ResourceLocation, List<RegistryObject<Item>>> item;

    public CreativeTabHelper() {
        this.item = new HashMap<>();
    }

    public void put(ResourceLocation resourceLocation, RegistryObject<Item> item) {
        this.item.computeIfAbsent(resourceLocation, resourceLocation1 -> {
            List<RegistryObject<Item>> items = new ArrayList<>();
            items.add(item);
            return items;
        });
    }
}

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

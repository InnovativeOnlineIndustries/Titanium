/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.corporis;

import com.hrznstudio.titanium.base.Titanium;
import com.hrznstudio.titanium.base.pulsar.control.PulseManager;
import com.hrznstudio.titanium.base.pulsar.pulse.Pulse;
import com.hrznstudio.titanium.base.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.corporis.compat.TinkersCompat;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Pulse(id = "corporis")
public class Corporis {
    public static List<ItemResource> RESOURCE_ITEMS = new ArrayList<>();
    public static AdvancedTitaniumTab RESOURCES_TAB;
    private static boolean vanilla;

    public Corporis(PulseManager compats) {
        compats.registerPulse(new TinkersCompat());
    }

    public static void registerVanillaMaterials() {
        if (!vanilla) {
            vanilla = true;
            ResourceRegistry.addMaterial("iron", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "iron"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
            ResourceRegistry.addMaterial("gold", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "gold"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        for (ResourceType type : ResourceType.values()) {
            if (type.hasMaterial()) {
                RESOURCE_ITEMS.add(new ItemResource(type));
            }
        }
        if (!RESOURCE_ITEMS.isEmpty()) {
            RESOURCES_TAB = new AdvancedTitaniumTab("titanium.resources", true);
            RESOURCE_ITEMS.forEach(item -> {
                event.getRegistry().register(item.setCreativeTab(RESOURCES_TAB));
                ResourceRegistry.getMaterials().forEach(material -> {
                    if (material.hasType(item.getType())) {
                        RESOURCES_TAB.addIconStacks(item.getStack(material, 1));
                        OreDictionary.registerOre(item.getType().getOreDict() + StringUtils.capitalize(material.materialName), item.getStack(material, 1));
                    }
                });
            });
        }
    }
}

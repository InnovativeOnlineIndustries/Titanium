/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium;

import com.hrznstudio.titanium.client.TitaniumModelLoader;
import com.hrznstudio.titanium.item.ItemBase;
import com.hrznstudio.titanium.resource.ItemResource;
import com.hrznstudio.titanium.resource.ResourceMaterial;
import com.hrznstudio.titanium.resource.ResourceRegistry;
import com.hrznstudio.titanium.resource.ResourceType;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.SidedHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = Titanium.MODID, name = Titanium.NAME, version = Titanium.VERSION)
public class Titanium {
    public static final String MODID = "titanium";
    public static final String NAME = "Titanium";
    public static final String VERSION = "1.0.0";

    public static AdvancedTitaniumTab RESOURCES_TAB;

    public static List<ItemResource> RESOURCE_ITEMS = new ArrayList<>();

    private static boolean vanilla;

    public static void registerVanillaMaterials() {
        if (!vanilla) {
            vanilla = true;
            ResourceRegistry.addMaterial("iron", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(MODID, "iron"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
            ResourceRegistry.addMaterial("gold", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(MODID, "gold"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        SidedHandler.runOn(Side.CLIENT, () -> () -> ModelLoaderRegistry.registerLoader(new TitaniumModelLoader()));
    }

    @SubscribeEvent
    public void registerItems(Register<Item> event) {
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

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void modelRegistry(ModelRegistryEvent event) {
        ItemBase.ITEMS.forEach(ItemBase::registerModels);
    }
}

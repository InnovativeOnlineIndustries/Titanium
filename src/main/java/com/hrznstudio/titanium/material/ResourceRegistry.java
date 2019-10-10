/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.google.common.collect.HashMultimap;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.annotation.MaterialReference;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;

public class ResourceRegistry {

    public static AdvancedTitaniumTab RESOURCES = new AdvancedTitaniumTab("resources", true);
    public static Logger LOGGER = LogManager.getLogger(Titanium.MODID + "-ResourceRegistry");

    private static HashMap<String, ResourceMaterial> MATERIALS = new HashMap<>();
    private static HashMap<String, HashMultimap<String, Field>> ANNOTATED_FIELDS = new HashMap<>();
    private static Field modifiersField;

    public static void onPreInit() {
        scanForReferences();
        ResourceTypeProperties.DEFAULTS.put(Block.class, new ResourceTypeProperties(Block.Properties.from(Blocks.IRON_ORE)));
        ResourceTypeProperties.DEFAULTS.put(Item.class, new ResourceTypeProperties(new Item.Properties().group(RESOURCES)));
        getOrCreate("iron").withOverride(ResourceType.ORE, Blocks.IRON_ORE).withOverride(ResourceType.METAL_BLOCK, Blocks.IRON_BLOCK).withOverride(ResourceType.INGOT, Items.IRON_INGOT).withOverride(ResourceType.NUGGET, Items.IRON_NUGGET);
        getOrCreate("gold").withOverride(ResourceType.ORE, Blocks.GOLD_ORE).withOverride(ResourceType.METAL_BLOCK, Blocks.GOLD_BLOCK).withOverride(ResourceType.INGOT, Items.GOLD_INGOT).withOverride(ResourceType.NUGGET, Items.GOLD_NUGGET);
        getOrCreate("coal").withOverride(ResourceType.ORE, Blocks.COAL_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.COAL_BLOCK).withOverride(ResourceType.GEM, Items.COAL);
        getOrCreate("lapis_lazuli").withOverride(ResourceType.ORE, Blocks.LAPIS_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.LAPIS_BLOCK).withOverride(ResourceType.GEM, Items.LAPIS_LAZULI);
        getOrCreate("diamond").withOverride(ResourceType.ORE, Blocks.DIAMOND_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.DIAMOND_BLOCK).withOverride(ResourceType.GEM, Items.DIAMOND);
        getOrCreate("redstone").withOverride(ResourceType.ORE, Blocks.REDSTONE_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.REDSTONE_BLOCK).withOverride(ResourceType.DUST, Items.REDSTONE);
        getOrCreate("emerald").withOverride(ResourceType.ORE, Blocks.EMERALD_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.EMERALD_BLOCK).withOverride(ResourceType.GEM, Items.EMERALD);
        getOrCreate("nether_quartz").withOverride(ResourceType.NETHER_ORE, Blocks.NETHER_QUARTZ_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.QUARTZ_BLOCK).withOverride(ResourceType.GEM, Items.QUARTZ);
        getOrCreate("glowstone").withOverride(ResourceType.GEM_BLOCK, Blocks.GLOWSTONE).withOverride(ResourceType.DUST, Items.GLOWSTONE_DUST);
        EventManager.forge(ColorHandlerEvent.Item.class).process(item -> {
            ResourceRegistry.getMaterials().forEach(material -> {
                material.getGenerated().values().stream().filter(entry -> entry instanceof IHasColor).forEach(entry -> {
                    if (entry instanceof Block) {
                        item.getBlockColors().register((state, world, pos, tint) -> ((IHasColor) entry).getColor(tint), (Block) entry);
                    } else if (entry instanceof Item) {
                        item.getItemColors().register((stack, tint) -> ((IHasColor) entry).getColor(tint), (IItemProvider) entry);
                    }
                });
            });
        }).subscribe();
    }

    private static void scanForReferences() {
        if (modifiersField == null) {
            try {
                modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                LOGGER.error(e);
            }
        }
        for (Field annotatedField : AnnotationUtil.getAnnotatedFields(MaterialReference.class)) {
            if (Modifier.isStatic(annotatedField.getModifiers())) {
                MaterialReference reference = annotatedField.getAnnotation(MaterialReference.class);
                if (!annotatedField.isAccessible()) {
                    annotatedField.setAccessible(true);
                }
                if (Modifier.isFinal(annotatedField.getModifiers())) {
                    try {
                        modifiersField.setInt(annotatedField, annotatedField.getModifiers() & ~Modifier.FINAL);
                    } catch (IllegalAccessException e) {
                        LOGGER.error(e);
                    }
                }
                ANNOTATED_FIELDS.computeIfAbsent(reference.material(), s -> HashMultimap.create()).put(reference.type(), annotatedField);
            }
        }
    }

    public static void initModules(ModuleController controller) {
        ResourceRegistry.getMaterials().forEach(material -> {
            Module.Builder builder = Module.builder("resources." + material.getMaterialType());
            if (material.getGeneratorTypes().size() > 0) {
                material.getGeneratorTypes().values().forEach(type -> {
                    ForgeRegistryEntry entry = material.generate(type);
                    if (entry != null) {
                        Feature.Builder feature = Feature.builder(type.getTag());
                        feature.content(entry.getRegistryType(), entry);
                        builder.feature(feature);
                    }
                });
            }
            controller.addModule(builder);
        });
    }

    public static void onPostInit() {
        ResourceRegistry.getMaterials().stream().map(material -> material.getGenerated().values()).flatMap(Collection::stream).
                filter(forgeRegistryEntry -> forgeRegistryEntry instanceof IItemProvider && ForgeRegistries.ITEMS.containsKey(forgeRegistryEntry.getRegistryName())).
                forEach(forgeRegistryEntry -> ResourceRegistry.RESOURCES.addIconStack(new ItemStack(((IItemProvider) forgeRegistryEntry).asItem())));
    }

    public static ResourceMaterial getOrCreate(String type) {
        return MATERIALS.computeIfAbsent(type, s -> new ResourceMaterial(type));
    }

    public static Collection<ResourceMaterial> getMaterials() {
        return MATERIALS.values();
    }

    public static HashMap<String, HashMultimap<String, Field>> getAnnotatedFields() {
        return ANNOTATED_FIELDS;
    }

    public static void injectField(ResourceMaterial material, IResourceType type, ForgeRegistryEntry entry) {
        if (ANNOTATED_FIELDS.containsKey(material.getMaterialType())) {
            HashMultimap<String, Field> multimap = ANNOTATED_FIELDS.get(material.getMaterialType());
            if (multimap.containsKey(type.getName())) {
                multimap.get(type.getName()).stream().filter(field -> entry.getRegistryType().isAssignableFrom(field.getType())).forEach(field -> {
                    try {
                        field.set(null, entry);
                    } catch (IllegalAccessException e) {
                        LOGGER.error(e);
                    }
                });
            }
        }
    }
}

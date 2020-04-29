/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

@FeaturePlugin(value = ResourceRegistry.PLUGIN_NAME, type = FeaturePlugin.FeaturePluginType.FEATURE)
public class DefaultResourceRegistryPlugin implements FeaturePluginInstance {

    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            construction();
        }
    }

    public void construction() {
        ResourceTypeProperties.DEFAULTS.put(Block.class, new ResourceTypeProperties(Block.Properties.from(Blocks.IRON_ORE)));
        ResourceTypeProperties.DEFAULTS.put(Item.class, new ResourceTypeProperties(new Item.Properties().group(ResourceRegistry.RESOURCES)));
        ResourceRegistry.getOrCreate("iron").setColor(0xd8d8d8).withOverride(ResourceType.ORE, Blocks.IRON_ORE).withOverride(ResourceType.METAL_BLOCK, Blocks.IRON_BLOCK).withOverride(ResourceType.INGOT, Items.IRON_INGOT).withOverride(ResourceType.NUGGET, Items.IRON_NUGGET);
        ResourceRegistry.getOrCreate("gold").setColor(0xfad64a).withOverride(ResourceType.ORE, Blocks.GOLD_ORE).withOverride(ResourceType.METAL_BLOCK, Blocks.GOLD_BLOCK).withOverride(ResourceType.INGOT, Items.GOLD_INGOT).withOverride(ResourceType.NUGGET, Items.GOLD_NUGGET);
        ResourceRegistry.getOrCreate("coal").setColor(0x363636).withOverride(ResourceType.ORE, Blocks.COAL_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.COAL_BLOCK).withOverride(ResourceType.GEM, Items.COAL);
        ResourceRegistry.getOrCreate("lapis_lazuli").setColor(0x345ec3).withOverride(ResourceType.ORE, Blocks.LAPIS_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.LAPIS_BLOCK).withOverride(ResourceType.GEM, Items.LAPIS_LAZULI);
        ResourceRegistry.getOrCreate("diamond").setColor(0x4aedd9).withOverride(ResourceType.ORE, Blocks.DIAMOND_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.DIAMOND_BLOCK).withOverride(ResourceType.GEM, Items.DIAMOND);
        ResourceRegistry.getOrCreate("redstone").setColor(0xaa0f01).withOverride(ResourceType.ORE, Blocks.REDSTONE_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.REDSTONE_BLOCK).withOverride(ResourceType.DUST, Items.REDSTONE);
        ResourceRegistry.getOrCreate("emerald").setColor(0x17dd62).withOverride(ResourceType.ORE, Blocks.EMERALD_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.EMERALD_BLOCK).withOverride(ResourceType.GEM, Items.EMERALD);
        ResourceRegistry.getOrCreate("nether_quartz").setColor(0xddd4c6).withOverride(ResourceType.NETHER_ORE, Blocks.NETHER_QUARTZ_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.QUARTZ_BLOCK).withOverride(ResourceType.GEM, Items.QUARTZ);
        ResourceRegistry.getOrCreate("glowstone").setColor(0xffbc5e).withOverride(ResourceType.GEM_BLOCK, Blocks.GLOWSTONE).withOverride(ResourceType.DUST, Items.GLOWSTONE_DUST);
    }
}

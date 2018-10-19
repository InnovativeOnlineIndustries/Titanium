/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import com.hrznstudio.titanium.api.resource.ResourceType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Random;

public class BlockOre extends BlockResource {
    private static Item grindingsItem;

    public BlockOre(ResourceType type, ResourceMaterial resourceMaterial) {
        super(type, resourceMaterial, Material.ROCK);
    }

    public static Item getGrindingsItem() {
        if (grindingsItem == null) {
            grindingsItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Titanium.MODID, "grindings"));
        }
        return grindingsItem == null ? Items.AIR : grindingsItem;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getGrindingsItem();
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return random.nextInt(2) + 2;
    }
}
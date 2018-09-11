/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import com.hrznstudio.titanium.api.resource.ResourceRegistry;
import com.hrznstudio.titanium.api.resource.ResourceType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class ItemResource extends ItemBase {
    private ResourceType type;

    public ItemResource(ResourceType type) {
        super(type.getName());
        this.type = type;
    }

    public ResourceType getType() {
        return type;
    }

    public void registerModels() {
        ResourceRegistry.getMaterials().forEach(material -> {
            if (material.hasType(this.type)) {
                ModelResourceLocation location = null;
                for (Function<ResourceType, ModelResourceLocation> function : material.getModelFunctions()) {
                    location = function.apply(type);
                    if (location != null)
                        break;
                }
                if (location != null) ModelLoader.setCustomModelResourceLocation(this, material.meta, location);
            }
        });
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(ItemStack stack) {
        return String.format(super.getItemStackDisplayName(stack), ResourceRegistry.getMaterial(stack.getMetadata()).filter(material -> material.hasType(this.type)).map(ResourceMaterial::getLocalizedName).orElse("Error!"));
    }

    public ItemStack getStack(ResourceMaterial material, int amount) {
        return new ItemStack(this, amount, material.meta);
    }

    @Override
    public void listSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        ResourceRegistry.getMaterials().forEach(material -> {
            if (!material.hasType(this.type))
                return;
            items.add(new ItemStack(this, 1, material.meta));
        });
    }
}
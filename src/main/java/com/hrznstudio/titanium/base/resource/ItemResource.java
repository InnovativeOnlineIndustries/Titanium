/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.resource;

import com.hrznstudio.titanium.base.item.ItemBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

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
            if (material.hasType(this.type))
                ModelLoader.setCustomModelResourceLocation(this, material.meta, material.getModelFunction().apply(type));
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
/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.item;

import com.google.common.collect.Lists;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemEnum<E extends Enum<E> & IStringSerializable> extends ItemBase {
    private List<E> values;

    public ItemEnum(String name, E... values) {
        super(name);
        this.values = Lists.newArrayList(values);
        setHasSubtypes(this.values.size() > 1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + values.get(stack.getMetadata()).getName();
    }

    @Override
    public void registerModels() {
        values.forEach(e -> ModelLoader.setCustomModelResourceLocation(this, values.indexOf(e), new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "type=" + e.getName())));
    }

    @Override
    public void listSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        values.forEach(e -> items.add(new ItemStack(this, 1, values.indexOf(e))));
    }
}
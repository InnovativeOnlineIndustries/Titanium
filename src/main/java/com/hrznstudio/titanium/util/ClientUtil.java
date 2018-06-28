/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ClientUtil {
    public static void registerToMapper(Block block) {
        if (block != null) {
            final String resourcePath = block.getRegistryName().toString();

            setCustomStateMapper(block, state -> new ModelResourceLocation(resourcePath, StateUtil.getPropertyString(state.getProperties())));

            NonNullList<ItemStack> subBlocks = NonNullList.create();
            block.getSubBlocks(null, subBlocks);

            for (ItemStack stack : subBlocks) {
                //noinspection deprecation
                IBlockState state = block.getStateFromMeta(stack.getMetadata());
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), stack.getMetadata(), new ModelResourceLocation(resourcePath, StateUtil.getPropertyString(state.getProperties())));
            }
        }
    }
    public static void registerToVariant(Block block, String variant) {
        if (block != null) {
            final String resourcePath = block.getRegistryName().toString();

            setCustomStateMapper(block, state -> new ModelResourceLocation(resourcePath, variant));

            NonNullList<ItemStack> subBlocks = NonNullList.create();
            block.getSubBlocks(null, subBlocks);

            for (ItemStack stack : subBlocks) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), stack.getMetadata(), new ModelResourceLocation(resourcePath, variant));
            }
        }
    }

    public static void setCustomStateMapper(Block block, StateMapper mapper) {
        ModelLoader.setCustomStateMapper(block, new DefaultStateMapper() {
            @Override
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return mapper.getModelResourceLocation(state);
            }
        });
    }

    public interface StateMapper {
        ModelResourceLocation getModelResourceLocation(IBlockState state);
    }
}

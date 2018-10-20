/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.internal.IModelRegistrar;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBase extends Item implements IModelRegistrar {
    public ItemBase(String name) {
        setRegistryName(name);
        setUnlocalizedName(Objects.requireNonNull(getRegistryName()).toString().replace(':', '.'));
    }

    @Override
    public int getMetadata(int damage) {
        return getHasSubtypes() ? damage : 0;
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            listSubItems(tab, items);
        }
    }

    public void listSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (hasDetails(null)) {
            addDetails(null, stack, tooltip, flagIn.isAdvanced());
        }
        for (Key key : Key.values()) {
            if (hasDetails(key)) {
                if (key.isDown()) {
                    addDetails(key, stack, tooltip, flagIn.isAdvanced());
                } else {
                    tooltip.add("Hold " + TextFormatting.YELLOW + key.getName() + TextFormatting.GRAY + " for more information");
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addDetails(@Nullable Key key, ItemStack stack, List<String> tooltip, boolean advanced) {

    }

    @SideOnly(Side.CLIENT)
    public boolean hasDetails(@Nullable Key key) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public enum Key implements IStringSerializable {
        SHIFT(Keyboard.KEY_RSHIFT, Keyboard.KEY_LSHIFT),
        CTRL(new int[]{Keyboard.KEY_RCONTROL, Keyboard.KEY_LCONTROL}, "CMD", new int[]{Keyboard.KEY_LMETA, Keyboard.KEY_RMETA}),
        ALT(Keyboard.KEY_LMENU, Keyboard.KEY_RMENU);

        final String name;
        int[] keys;

        Key(int... keys) {
            this.keys = keys;
            this.name = name();
        }

        Key(int[] keysWin, String macName, int[] keysMac) {
            if (Minecraft.IS_RUNNING_ON_MAC) {
                this.keys = keysMac;
                this.name = macName;
            } else {
                this.keys = keysWin;
                this.name = name();
            }
        }

        public boolean isDown() {
            for (int key : keys)
                if (Keyboard.isKeyDown(key))
                    return true;
            return false;
        }

        @Override
        @Nonnull
        public String getName() {
            return StringUtils.capitalize(name.toLowerCase());
        }
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class BasicItem extends Item {

    private CreativeModeTab itemGroup = CreativeModeTab.TAB_SEARCH;

    public BasicItem(Properties properties) {
        super(properties);
    }

    public BasicItem(String name, Properties properties) {
        super(properties);
        //setRegistryName(name);
    }

    @Override
    public final void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (hasTooltipDetails(null)) {
            addTooltipDetails(null, stack, tooltip, flagIn.isAdvanced());
        }
        for (Key key : Key.values()) {
            if (hasTooltipDetails(key)) {
                if (key.isDown()) {
                    addTooltipDetails(key, stack, tooltip, flagIn.isAdvanced());
                } else {
                    tooltip.add(new TextComponent("Hold " + ChatFormatting.YELLOW + key.getSerializedName() + ChatFormatting.GRAY + " for more information"));
                }
            }
        }
    }

    public void addTooltipDetails(@Nullable Key key, ItemStack stack, List<Component> tooltip, boolean advanced) {

    }

    public boolean hasTooltipDetails(@Nullable Key key) {
        return false;
    }

    public void setItemGroup(CreativeModeTab itemGroup) {
        this.itemGroup = itemGroup;
    }

    public enum Key implements StringRepresentable {
        SHIFT(GLFW.GLFW_KEY_RIGHT_SHIFT, GLFW.GLFW_KEY_LEFT_SHIFT),
        CTRL(GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_LEFT_CONTROL),
        ALT(GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_LEFT_ALT);

        final String name;
        int[] keys;

        Key(int... keys) {
            this.keys = keys;
            this.name = name();
        }

        Key(int[] keysWin, String macName, int[] keysMac) {
            if (Minecraft.ON_OSX) {
                this.keys = keysMac;
                this.name = macName;
            } else {
                this.keys = keysWin;
                this.name = name();
            }
        }

        public boolean isDown() {
            for (int key : keys)
                if (GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key) == GLFW.GLFW_PRESS) //Main windows
                    return true;
            return false;
        }

        // getName
        @Override
        @Nonnull
        public String getSerializedName() {
            return StringUtils.capitalize(name.toLowerCase());
        }
    }
}

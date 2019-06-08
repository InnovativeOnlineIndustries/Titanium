/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

//import net.minecraft.util.text.TextFormatting;

@ParametersAreNonnullByDefault
public class ItemBase extends Item {

    private ItemGroup itemGroup = ItemGroup.SEARCH;

    public ItemBase(String name, Properties properties) {
        super(properties);
        setRegistryName(name);
    }

    @Override
    public final void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (hasTooltipDetails(null)) {
            addTooltipDetails(null, stack, tooltip, flagIn.isAdvanced());
        }
        for (Key key : Key.values()) {
            if (hasTooltipDetails(key)) {
                if (key.isDown()) {
                    addTooltipDetails(key, stack, tooltip, flagIn.isAdvanced());
                } else {
                    tooltip.add(new StringTextComponent("Hold " + TextFormatting.YELLOW + key.getName() + TextFormatting.GRAY + " for more information"));
                }
            }
        }
    }

    public void addTooltipDetails(@Nullable Key key, ItemStack stack, List<ITextComponent> tooltip, boolean advanced) {

    }

    public boolean hasTooltipDetails(@Nullable Key key) {
        return false;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public enum Key implements IStringSerializable {
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
                if (GLFW.glfwGetKey(Minecraft.getInstance().mainWindow.getHandle(), key) == GLFW.GLFW_PRESS)
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
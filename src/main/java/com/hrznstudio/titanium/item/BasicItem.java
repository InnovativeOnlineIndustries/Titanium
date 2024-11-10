/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
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

    private TitaniumTab itemGroup = null;

    public BasicItem(Properties properties) {
        super(properties);
    }

    public BasicItem(String name, Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (hasTooltipDetails(null)) {
            addTooltipDetails(null, stack, tooltip, flagIn.isAdvanced());
        }
        for (Key key : Key.values()) {
            if (hasTooltipDetails(key)) {
                if (key.isDown()) {
                    addTooltipDetails(key, stack, tooltip, flagIn.isAdvanced());
                } else {
                    String keyName = ChatFormatting.YELLOW + key.getSerializedName() + ChatFormatting.GRAY;
                    tooltip.add(Component.literal(Component.translatable("tooltip.titanium.hold_moreinfo",keyName).getString()));
                }
            }
        }
    }

    public void addTooltipDetails(@Nullable Key key, ItemStack stack, List<Component> tooltip, boolean advanced) {

    }

    public boolean hasTooltipDetails(@Nullable Key key) {
        return false;
    }

    public void setItemGroup(TitaniumTab itemGroup) {
        this.itemGroup = itemGroup;
        this.itemGroup.getTabList().add(this);
    }

    public enum Key implements StringRepresentable {
        SHIFT(GLFW.GLFW_KEY_RIGHT_SHIFT, GLFW.GLFW_KEY_LEFT_SHIFT),
        CTRL(GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_LEFT_CONTROL),
        ALT(GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_LEFT_ALT);

        final String name;
        final int[] keys;

        Key(int... keys) {
            this.keys = keys;
            this.name = name();
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

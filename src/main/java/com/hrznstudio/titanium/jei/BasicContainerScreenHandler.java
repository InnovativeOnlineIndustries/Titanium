/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.jei;


import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BasicContainerScreenHandler<T extends AbstractContainerMenu> implements IGuiContainerHandler<BasicContainerScreen<T>> {

    @Override
    @Nonnull
    public List<Rect2i> getGuiExtraAreas(BasicContainerScreen<T> containerScreen) {
        List<Rect2i> rectangles = new ArrayList<>();
        for (Object o : containerScreen.getAddons()) {
            if (o instanceof BasicScreenAddon) {
                BasicScreenAddon addon = (BasicScreenAddon) o;
                rectangles.add(new Rect2i(containerScreen.getX() + addon.getPosX(), containerScreen.getY() + addon.getPosY(), addon.getXSize(), addon.getYSize()));
            }
        }
        return rectangles;
    }
}


/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.jei;

import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerScreenHandler implements IGuiContainerHandler<BasicContainerScreen> {

    @Override
    public List<Rectangle2d> getGuiExtraAreas(BasicContainerScreen containerScreen) {
        List<Rectangle2d> rectangles = new ArrayList<>();
        for (Object o : containerScreen.getAddons()) {
            if (o instanceof BasicScreenAddon) {
                BasicScreenAddon addon = (BasicScreenAddon) o;
                rectangles.add(new Rectangle2d(containerScreen.getX() + addon.getPosX(), containerScreen.getY() + addon.getPosY(), addon.getXSize(), addon.getYSize()));
            }
        }
        return rectangles;
    }
}

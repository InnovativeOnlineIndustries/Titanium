/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.jei;

import com.hrznstudio.titanium.client.gui.addon.BasicGuiAddon;
import com.hrznstudio.titanium.client.gui.container.GuiContainerBase;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerScreenHandler implements IGuiContainerHandler<GuiContainerBase> {

    @Override
    public List<Rectangle2d> getGuiExtraAreas(GuiContainerBase containerScreen) {
        List<Rectangle2d> rectangles = new ArrayList<>();
        for (Object o : containerScreen.getAddons()) {
            if (o instanceof BasicGuiAddon) {
                BasicGuiAddon addon = (BasicGuiAddon) o;
                rectangles.add(new Rectangle2d(containerScreen.getX() + addon.getPosX(), containerScreen.getY() + addon.getPosY(), addon.getXSize(), addon.getYSize()));
            }
        }
        return rectangles;
    }
}

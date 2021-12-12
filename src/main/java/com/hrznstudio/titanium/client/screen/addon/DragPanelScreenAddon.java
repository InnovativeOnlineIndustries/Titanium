/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import net.minecraft.util.Mth;

public abstract class DragPanelScreenAddon extends BasicScreenAddon {

    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;
    private final int panelSizeX;
    private final int panelSizeY;
    private int scrollX;
    private int scrollY;

    protected DragPanelScreenAddon(int posX, int posY, int sizeX, int sizeY, int panelSizeX, int panelSizeY) {
        super(posX, posY);
        this.panelSizeX = panelSizeX;
        this.panelSizeY = panelSizeY;
        this.minX = 0;
        this.maxX = sizeX;
        this.minY = 0;
        this.maxY = sizeY;
        this.scrollX = this.scrollY = 0;
    }

    @Override
    public int getXSize() {
        return panelSizeX;
    }

    @Override
    public int getYSize() {
        return panelSizeY;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        this.scrollX = (int) Math.round(Mth.clamp(this.scrollX - mouseX, this.minX, this.maxX));
        this.scrollY = (int) Math.round(Mth.clamp(this.scrollY - mouseY, this.minY, this.maxY));
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public int getScrollX() {
        return scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

}

/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.cassandra.client.gui.addon;

import net.minecraft.util.math.MathHelper;

public abstract class DragPanelGuiAddon extends BasicGuiAddon implements ICanMouseDrag {

    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;
    private final int panelSizeX;
    private final int panelSizeY;
    private int scrollX;
    private int scrollY;

    protected DragPanelGuiAddon(int posX, int posY, int sizeX, int sizeY, int panelSizeX, int panelSizeY) {
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
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }

    @Override
    public void drag(int x, int y) {
        this.scrollX = MathHelper.clamp(this.scrollX + x, this.minX, this.maxX);
        this.scrollY = MathHelper.clamp(this.scrollY + y, this.minY, this.maxY);
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

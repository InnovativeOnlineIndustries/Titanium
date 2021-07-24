/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.filter;

import net.minecraft.world.item.DyeColor;

public class FilterSlot<T extends Object> {

    private int x;
    private int y;
    private T filter;
    private int filterID;
    private int color;

    public FilterSlot(int x, int y, int filterID, T defaultFilter) {
        this.x = x;
        this.y = y;
        this.filterID = filterID;
        this.filter = defaultFilter;
        setColor(DyeColor.WHITE);
    }

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    public int getFilterID() {
        return filterID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(DyeColor color) {
        this.color = color.getFireworkColor();
    }

}

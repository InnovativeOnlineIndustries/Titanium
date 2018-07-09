/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;

public abstract class BasicGuiAddon implements IGuiAddon {

    private int posX;
    private int posY;

    protected BasicGuiAddon(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public boolean isInside(GuiContainerTile container, int mouseX, int mouseY) {
        return mouseX > this.getPosX() && mouseX < this.getPosX() + getXSize() && mouseY > this.getPosY() && mouseY < this.getPosY() + getYSize();
    }

    public abstract int getXSize();

    public abstract int getYSize();

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}

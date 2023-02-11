/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.IScreenAddon;

public abstract class BasicScreenAddon implements IScreenAddon {

    private int posX;
    private int posY;

    protected BasicScreenAddon(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.getPosX() && mouseX <= this.getPosX() + getXSize() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + getYSize();
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

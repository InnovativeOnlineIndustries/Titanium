/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import net.minecraft.client.gui.GuiScreen;

public abstract class BasicGuiAddon implements IGuiAddon {

    private int posX;
    private int posY;

    protected BasicGuiAddon(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public boolean isInside(GuiScreen container, double mouseX, double mouseY) {
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

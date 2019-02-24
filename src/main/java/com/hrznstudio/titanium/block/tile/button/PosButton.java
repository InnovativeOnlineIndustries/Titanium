/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block.tile.button;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Predicate;

public class PosButton {

    private final int posX;
    private final int posY;
    private final int sizeX;
    private final int sizeY;
    private int id;
    private Predicate<NBTTagCompound> serverPredicate;

    public PosButton(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public PosButton setPredicate(Predicate<NBTTagCompound> serverPredicate) {
        this.serverPredicate = serverPredicate;
        return this;
    }

    public void onButtonClicked(NBTTagCompound information) {
        if (serverPredicate != null) serverPredicate.test(information);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getId() {
        return id;
    }

    public PosButton setId(int id) {
        this.id = id;
        return this;
    }
}

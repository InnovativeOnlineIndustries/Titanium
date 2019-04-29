/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.block.tile.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.client.gui.addon.BasicButtonAddon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PosButton implements IGuiAddonProvider {

    private final int posX;
    private final int posY;
    private final int sizeX;
    private final int sizeY;
    private int id;
    private Consumer<NBTTagCompound> serverPredicate;

    public PosButton(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    /**
     * Sets a predicate that will be runned in the server when the button is clicked in the client
     *
     * @param serverPredicate A predicate that has a NBTTagCompound with client information
     * @return itself
     */
    public PosButton setPredicate(Consumer<NBTTagCompound> serverPredicate) {
        this.serverPredicate = serverPredicate;
        return this;
    }

    public void onButtonClicked(NBTTagCompound information) {
        if (serverPredicate != null) serverPredicate.accept(information);
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

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Collections.singletonList(() -> new BasicButtonAddon(this));
    }
}

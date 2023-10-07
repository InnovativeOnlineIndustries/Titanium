/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.client.screen.addon.BasicButtonAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class ButtonComponent implements IScreenAddonProvider {

    private final int posX;
    private final int posY;
    private final int sizeX;
    private final int sizeY;
    private int id;
    private BiConsumer<PlayerEntity, CompoundNBT> serverPredicate;

    public ButtonComponent(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.id = -1;
    }

    /**
     * Sets a predicate that will be runned in the server when the button is clicked in the client
     *
     * @param serverPredicate A predicate that has a NBTTagCompound with client information
     * @return itself
     */
    public ButtonComponent setPredicate(BiConsumer<PlayerEntity, CompoundNBT> serverPredicate) {
        this.serverPredicate = serverPredicate;
        return this;
    }

    public void onButtonClicked(PlayerEntity entity, CompoundNBT information) {
        if (serverPredicate != null) {
            serverPredicate.accept(entity, information);
        }
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

    public ButtonComponent setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new BasicButtonAddon(this));
    }
}

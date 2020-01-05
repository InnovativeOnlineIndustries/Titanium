/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

public class MultiButtonHandler implements IGuiAddonProvider {

    private List<PosButton> basicButtonAddons;

    public MultiButtonHandler() {
        basicButtonAddons = new ArrayList<>();
    }

    public void addButton(PosButton buttonAddon) {
        basicButtonAddons.add(buttonAddon.setId(basicButtonAddons.size()));
    }

    public void clickButton(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        basicButtonAddons.stream().filter(buttonAddon -> buttonAddon.getId() == id).forEach(buttonAddon -> buttonAddon.onButtonClicked(playerEntity, compound));
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        for (PosButton basicButtonAddon : basicButtonAddons) {
            List<IFactory<? extends IGuiAddon>> addon = basicButtonAddon.getGuiAddons();
            if (addon != null) addons.addAll(addon);
        }
        return addons;
    }

}

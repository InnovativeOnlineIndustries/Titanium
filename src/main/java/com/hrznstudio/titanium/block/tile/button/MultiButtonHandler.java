/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block.tile.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import net.minecraft.nbt.NBTTagCompound;

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

    public void clickButton(int id, NBTTagCompound compound) {
        basicButtonAddons.stream().filter(buttonAddon -> buttonAddon.getId() == id).forEach(buttonAddon -> buttonAddon.onButtonClicked(compound));
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

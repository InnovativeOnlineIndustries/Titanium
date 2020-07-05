/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

public class MultiButtonComponent implements IScreenAddonProvider, IComponentHandler<ButtonComponent> {

    private List<ButtonComponent> basicButtonAddons;

    public MultiButtonComponent() {
        basicButtonAddons = new ArrayList<>();
    }


    public void clickButton(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        basicButtonAddons.stream()
                .filter(buttonAddon -> buttonAddon.getId() == id)
                .forEach(buttonAddon -> buttonAddon.onButtonClicked(playerEntity, compound));
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        for (ButtonComponent basicButtonAddon : basicButtonAddons) {
            List<IFactory<? extends IScreenAddon>> addon = basicButtonAddon.getScreenAddons();
            if (addon != null) addons.addAll(addon);
        }
        return addons;
    }

    @Override
    public void add(ButtonComponent component) {
        if (component.getId() == -1) component.setId(basicButtonAddons.size());
        basicButtonAddons.add(component);
    }

    @Override
    public boolean accepts(Object component) {
        return component instanceof ButtonComponent;
    }
}

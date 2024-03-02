/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiButtonComponent implements IScreenAddonProvider, IComponentHandler {

    private List<ButtonComponent> basicButtonAddons;

    public MultiButtonComponent() {
        basicButtonAddons = new ArrayList<>();
    }


    public void clickButton(int id, Player playerEntity, CompoundTag compound) {
        basicButtonAddons.stream()
            .filter(buttonAddon -> buttonAddon.getId() == id)
            .forEach(buttonAddon -> buttonAddon.onButtonClicked(playerEntity, compound));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        for (ButtonComponent basicButtonAddon : basicButtonAddons) {
            List<IFactory<? extends IScreenAddon>> addon = basicButtonAddon.getScreenAddons();
            if (addon != null) addons.addAll(addon);
        }
        return addons;
    }

    @Override
    public void add(Object... component) {
        Arrays.stream(component).filter(this::accepts).map(o -> (ButtonComponent) o).forEach(buttonComponent -> {
            if (buttonComponent.getId() == -1) {
                buttonComponent.setId(basicButtonAddons.size());
            }
            basicButtonAddons.add(buttonComponent);
        });
    }

    private boolean accepts(Object component) {
        return component instanceof ButtonComponent;
    }
}

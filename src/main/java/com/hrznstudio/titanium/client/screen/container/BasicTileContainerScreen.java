/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.container.impl.BasicTileContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.stream.Collectors;

public class BasicTileContainerScreen extends BasicContainerScreen<BasicTileContainer<?>> implements IHasContainer<BasicTileContainer<?>> {
    public BasicTileContainerScreen(BasicTileContainer<?> basicTileContainer, PlayerInventory inventory, ITextComponent title) {
        super(basicTileContainer, inventory, title, basicTileContainer.getAssetProvider());
        setAddons(basicTileContainer.getTile()
                .getScreenAddons()
                .stream()
                .map(IFactory::create)
                .collect(Collectors.toList()));
    }
}

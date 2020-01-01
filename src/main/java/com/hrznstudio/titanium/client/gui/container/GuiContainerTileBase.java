/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.container.impl.ContainerTileBase;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiContainerTileBase extends GuiContainerBase<ContainerTileBase<?>> implements IHasContainer<ContainerTileBase<?>> {
    public GuiContainerTileBase(ContainerTileBase<?> containerTileBase, PlayerInventory inventory, ITextComponent title) {
        super(containerTileBase, inventory, title, containerTileBase.getAssetProvider());
        setAddons(containerTileBase.getTile()
                .getGuiAddons()
                .stream()
                .map(IFactory::create)
                .collect(Collectors.toList()));
    }
}

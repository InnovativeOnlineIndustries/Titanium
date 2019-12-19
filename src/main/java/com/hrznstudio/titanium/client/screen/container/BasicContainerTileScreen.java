/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.container.impl.ContainerTileBase;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.stream.Collectors;

public class BasicContainerTileScreen<T extends ActiveTile<?, T>> extends BasicContainerScreen<ContainerTileBase<T>> implements IHasContainer<ContainerTileBase<T>> {
    private final ContainerTileBase<T> containerTileBase;

    public BasicContainerTileScreen(ContainerTileBase<T> containerTileBase, PlayerInventory inventory, ITextComponent title) {
        super(containerTileBase, inventory, title, containerTileBase.getAssetProvider());
        this.containerTileBase = containerTileBase;
        setAddons(containerTileBase.getTile()
                .getAddons()
                .stream()
                .map(IFactory::create)
                .collect(Collectors.toList()));
    }
}

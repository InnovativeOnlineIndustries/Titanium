/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class BasicAddonScreen extends BasicContainerScreen<BasicAddonContainer> {
    public BasicAddonScreen(BasicAddonContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title, container.getAssetProvider());
        if (container.getProvider() instanceof IScreenAddonProvider) {
            ((IScreenAddonProvider) container.getProvider()).getScreenAddons()
                    .stream()
                    .map(IFactory::create)
                    .forEach(this.getAddons()::add);
        }
    }
}

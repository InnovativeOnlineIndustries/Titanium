/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BasicAddonScreen extends BasicContainerScreen<BasicAddonContainer> {
    public BasicAddonScreen(BasicAddonContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, container.getAssetProvider());
        if (container.getProvider() instanceof IScreenAddonProvider) {
            ((IScreenAddonProvider) container.getProvider()).getScreenAddons()
                .stream()
                .map(IFactory::create)
                .forEach(this.getAddons()::add);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.getMenu().update();
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    }
}

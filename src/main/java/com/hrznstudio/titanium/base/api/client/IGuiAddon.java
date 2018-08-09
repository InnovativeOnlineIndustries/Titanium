/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.api.client;

import com.hrznstudio.titanium.cassandra.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.cassandra.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;

import java.util.Collections;
import java.util.List;

public interface IGuiAddon {

    void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY);

    default List<String> getTooltipLines() {
        return Collections.emptyList();
    }

    boolean isInside(GuiContainerTile container, int mouseX, int mouseY);

}

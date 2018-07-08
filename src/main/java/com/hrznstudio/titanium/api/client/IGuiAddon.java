/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.gui.GuiContainerTile;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public interface IGuiAddon {

    void drawGuiContainerBackgroundLayer(GuiContainerTile container, float partialTicks, int mouseX, int mouseY);

    void drawGuiContainerForegroundLayer(GuiContainerTile container, int mouseX, int mouseY);

    default List<String> getTooltipLines() {
        return Collections.emptyList();
    }

    boolean isInside(GuiContainerTile container, int mouseX, int mouseY);

}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;

import java.util.Collections;
import java.util.List;

public interface IGuiAddon {

    void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY);

    default List<String> getTooltipLines() {
        return Collections.emptyList();
    }

    boolean isInside(GuiScreen screen, double mouseX, double mouseY);

}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;

import java.util.Collections;
import java.util.List;

public interface IGuiAddon {
    /**
     * Draws the component in the background layer
     *
     * @param screen       The current open screen
     * @param provider     The current asset provider used in the GUI
     * @param guiX         The gui X in the top left corner
     * @param guiY         The gui Y in the top left corner
     * @param mouseX       The current mouse X
     * @param mouseY       The current mouse Y
     * @param partialTicks Partial ticks
     */
    void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    /**
     * Draws the component in the foreground layer
     *
     * @param screen   The current open screen
     * @param provider The current asset provider used in the GUI
     * @param guiX     The gui X in the top left corner
     * @param guiY     The gui Y in the top left corner
     * @param mouseX   The current mouse X
     * @param mouseY   The current mouse Y
     */
    void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY);

    /**
     * A list of strings that will be drawn as a tooltip when `isInside` returns true
     *
     * @return A list of strings
     */
    default List<String> getTooltipLines() {
        return Collections.emptyList();
    }

    /**
     * A check to know if the mouse is inside of the component to draw the tooltip lines
     *
     * @param screen The current open screen
     * @param mouseX The current mouse X
     * @param mouseY The current mouse Y
     * @return True if the mouse is inside the component
     */
    boolean isInside(GuiScreen screen, double mouseX, double mouseY);

}

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;

import java.util.Collections;
import java.util.List;

public interface IScreenAddon {
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
    void drawBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

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
    void drawForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY);

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
    boolean isInside(Screen screen, double mouseX, double mouseY);

    /**
     * Called when a key is pressed
     * @param key The key pressed
     * @param scan ??
     * @param modifiers ??
     * @return if something was done
     */
    default boolean keyPressed(int key, int scan, int modifiers) {
        return false;
    }

    /**
     * Called when init is called in the screen.
     *
     * @param screenX the left point of the Screen
     * @param screenY the top point of the Screen
     */
    default void init(int screenX, int screenY) {

    }
}

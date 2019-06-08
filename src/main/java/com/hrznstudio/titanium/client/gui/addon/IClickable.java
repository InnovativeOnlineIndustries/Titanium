/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import net.minecraft.client.gui.screen.Screen;

public interface IClickable {

    void handleClick(Screen tile, int guiX, int guiY, double mouseX, double mouseY, int button);

}

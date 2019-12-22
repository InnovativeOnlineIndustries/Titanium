/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.ArrowButtonGuiAddon;
import com.hrznstudio.titanium.util.FacingUtil;

import java.util.Collections;
import java.util.List;

public class ArrowButtonComponent extends ButtonComponent {

    public final FacingUtil.Sideness direction;

    public ArrowButtonComponent(int posX, int posY, int sizeX, int sizeY, FacingUtil.Sideness direction) {
        super(posX, posY, sizeX, sizeY);
        this.direction = direction;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Collections.singletonList(() -> new ArrowButtonGuiAddon(this));
    }

    public FacingUtil.Sideness getDirection() {
        return direction;
    }
}

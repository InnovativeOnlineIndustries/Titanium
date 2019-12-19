/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.ArrowButtonScreenAddon;
import com.hrznstudio.titanium.util.FacingUtil;

import java.util.Collections;
import java.util.List;

public class ArrowButton extends PosButton {

    public final FacingUtil.Sideness direction;

    public ArrowButton(int posX, int posY, int sizeX, int sizeY, FacingUtil.Sideness direction) {
        super(posX, posY, sizeX, sizeY);
        this.direction = direction;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getAddons() {
        return Collections.singletonList(() -> new ArrowButtonScreenAddon(this));
    }

    public FacingUtil.Sideness getDirection() {
        return direction;
    }
}

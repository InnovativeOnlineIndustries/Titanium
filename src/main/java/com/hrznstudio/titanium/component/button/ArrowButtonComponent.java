/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.ArrowButtonScreenAddon;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrowButtonComponent extends ButtonComponent {

    public final FacingUtil.Sideness direction;
    private boolean isVisible = true;

    public ArrowButtonComponent(int posX, int posY, int sizeX, int sizeY, FacingUtil.Sideness direction) {
        super(posX, posY, sizeX, sizeY);
        this.direction = direction;
    }

    public void show() {
        this.isVisible = true;
    }

    public void hide() {
        this.isVisible = false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        if (isVisible) addons.add(() -> new ArrowButtonScreenAddon(this));
        return addons;
    }

    public FacingUtil.Sideness getDirection() {
        return direction;
    }
}

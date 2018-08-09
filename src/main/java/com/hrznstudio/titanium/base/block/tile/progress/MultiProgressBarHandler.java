/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.block.tile.progress;

import com.hrznstudio.titanium.base.api.IFactory;
import com.hrznstudio.titanium.base.api.client.IGuiAddon;
import com.hrznstudio.titanium.base.api.client.IGuiAddonProvider;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressBarHandler implements IGuiAddonProvider {

    private final List<PosProgressBar> posWorkBars;

    public MultiProgressBarHandler() {
        posWorkBars = new ArrayList<>();
    }

    public void addBar(PosProgressBar bar) {
        this.posWorkBars.add(bar);
    }

    public void update() {
        for (PosProgressBar posWorkBar : posWorkBars) {
            if (posWorkBar.getCanIncrease().test(posWorkBar.getTileBase())) {
                posWorkBar.increase();
            } else if (posWorkBar.getCanReset().test(posWorkBar.getTileBase())) {
                posWorkBar.setProgress(0);
            }
        }
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> list = new ArrayList<>();
        for (PosProgressBar posWorkBar : posWorkBars) {
            list.addAll(posWorkBar.getGuiAddons());
        }
        return list;
    }
}

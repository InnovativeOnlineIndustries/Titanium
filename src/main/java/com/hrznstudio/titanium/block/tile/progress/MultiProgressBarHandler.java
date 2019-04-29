/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.block.tile.progress;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;

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
                if (posWorkBar.getProgress() == 0) {
                    posWorkBar.onStart();
                }
                posWorkBar.tickBar();
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

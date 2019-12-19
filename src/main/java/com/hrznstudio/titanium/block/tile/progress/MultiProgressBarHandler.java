/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.progress;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressBarHandler<T extends IProgressing> implements IScreenAddonProvider {

    private final List<PosProgressBar<T>> posWorkBars;

    public MultiProgressBarHandler() {
        posWorkBars = new ArrayList<>();
    }

    public void addBar(PosProgressBar<T> bar) {
        this.posWorkBars.add(bar);
    }

    public void update() {
        for (PosProgressBar<T> posWorkBar : posWorkBars) {
            if (posWorkBar.getCanIncrease().test(posWorkBar.getProgressible())) {
                if (posWorkBar.getIncreaseType() && posWorkBar.getProgress() == 0) {
                    posWorkBar.onStart();
                }
                if (!posWorkBar.getIncreaseType() && posWorkBar.getProgress() == posWorkBar.getMaxProgress()) {
                    posWorkBar.onStart();
                }
                posWorkBar.tickBar();
            } else if (posWorkBar.getCanReset().test(posWorkBar.getProgressible())) {
                posWorkBar.setProgress(posWorkBar.getIncreaseType() ? 0 : posWorkBar.getMaxProgress());
            }
        }
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getAddons() {
        List<IFactory<? extends IScreenAddon>> list = new ArrayList<>();
        for (PosProgressBar<T> posWorkBar : posWorkBars) {
            list.addAll(posWorkBar.getAddons());
        }
        return list;
    }
}

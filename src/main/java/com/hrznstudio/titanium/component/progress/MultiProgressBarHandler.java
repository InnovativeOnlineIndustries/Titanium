/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.progress;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressBarHandler<T extends IComponentHarness> implements IScreenAddonProvider {

    private final List<ProgressBarComponent<T>> posWorkBars;

    public MultiProgressBarHandler() {
        posWorkBars = new ArrayList<>();
    }

    public void addBar(ProgressBarComponent<T> bar) {
        this.posWorkBars.add(bar);
    }

    public void update() {
        for (ProgressBarComponent<T> posWorkBar : posWorkBars) {
            if (posWorkBar.getCanIncrease().test(posWorkBar.getComponentHarness())) {
                if (posWorkBar.getIncreaseType() && posWorkBar.getProgress() == 0) {
                    posWorkBar.onStart();
                }
                if (!posWorkBar.getIncreaseType() && posWorkBar.getProgress() == posWorkBar.getMaxProgress()) {
                    posWorkBar.onStart();
                }
                posWorkBar.tickBar();
            } else if (posWorkBar.getCanReset().test(posWorkBar.getComponentHarness())) {
                posWorkBar.setProgress(posWorkBar.getIncreaseType() ? 0 : posWorkBar.getMaxProgress());
            }
        }
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> list = new ArrayList<>();
        for (ProgressBarComponent<T> posWorkBar : posWorkBars) {
            list.addAll(posWorkBar.getScreenAddons());
        }
        return list;
    }
}

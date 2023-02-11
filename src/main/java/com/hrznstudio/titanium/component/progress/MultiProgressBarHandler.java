/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.progress;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHandler;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiProgressBarHandler<T extends IComponentHarness> implements IScreenAddonProvider, IContainerAddonProvider, IComponentHandler {

    private final List<ProgressBarComponent<T>> progressBarComponents;

    public MultiProgressBarHandler() {
        progressBarComponents = new ArrayList<>();
    }

    public void add(Object... components) {
        Arrays.stream(components).filter(this::accepts).forEach(o -> this.progressBarComponents.add((ProgressBarComponent<T>) o));
    }

    public void update() {
        for (ProgressBarComponent<T> posWorkBar : progressBarComponents) {
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
        for (ProgressBarComponent<T> progressBarComponent : progressBarComponents) {
            list.addAll(progressBarComponent.getScreenAddons());
        }
        return list;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> list = new ArrayList<>();
        for (ProgressBarComponent<T> progressBarComponent : progressBarComponents) {
            list.addAll(progressBarComponent.getContainerAddons());
        }
        return list;
    }

    private boolean accepts(Object component) {
        return component instanceof ProgressBarComponent;
    }
}

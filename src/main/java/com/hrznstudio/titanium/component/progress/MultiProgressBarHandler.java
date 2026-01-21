/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
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
        final int size = progressBarComponents.size();
        if (size == 0) return;

        // Кешируем gameTime один раз для всех баров
        long gameTime = -1;

        for (int i = 0; i < size; i++) {
            ProgressBarComponent<T> bar = progressBarComponents.get(i);
            T harness = bar.getComponentHarness();

            // Проверка tickingTime - вынесена из tickBar() чтобы избежать повторных getGameTime()
            int tickingTime = bar.getTickingTime();
            if (tickingTime > 1) {
                if (gameTime == -1 && harness != null) {
                    gameTime = harness.getComponentWorld().getGameTime();
                }
                if (gameTime != -1 && gameTime % tickingTime != 0) {
                    continue;
                }
            }

            if (bar.getCanIncrease().test(harness)) {
                boolean increaseType = bar.getIncreaseType();
                int progress = bar.getProgress();
                int maxProgress = bar.getMaxProgress();

                // Проверка onStart
                if (increaseType ? progress == 0 : progress == maxProgress) {
                    bar.onStart();
                }

                // Inline tickBar логика - избегаем повторного получения increaseType/progress/maxProgress
                bar.tickBarDirect(increaseType, progress, maxProgress, harness);
            } else if (bar.getCanReset().test(harness)) {
                bar.setProgress(bar.getIncreaseType() ? 0 : bar.getMaxProgress());
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

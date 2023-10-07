/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.referenceholder;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.util.IIntArray;

public class ProgressBarReferenceHolder implements IIntArray {
    private final ProgressBarComponent<?> progressBarComponent;

    public ProgressBarReferenceHolder(ProgressBarComponent<?> progressBarComponent) {
        this.progressBarComponent = progressBarComponent;
    }

    @Override
    public int get(int index) {
        if (index == 0) {
            return progressBarComponent.getProgress();
        } else if (index == 1) {
            return progressBarComponent.getMaxProgress();
        } else {
            return progressBarComponent.getProgressIncrease();
        }
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            progressBarComponent.setProgress(value);
        } else if (index == 1) {
            progressBarComponent.setMaxProgress(value);
        } else {
            progressBarComponent.setProgressIncrease(value);
        }
    }

    @Override
    public int size() {
        return 3;
    }
}

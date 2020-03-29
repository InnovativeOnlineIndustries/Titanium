/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
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
        return index == 0 ? progressBarComponent.getProgress() : progressBarComponent.getMaxProgress();
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            progressBarComponent.setProgress(value);
        } else {
            progressBarComponent.setMaxProgress(value);
        }
    }

    @Override
    public int size() {
        return 2;
    }
}

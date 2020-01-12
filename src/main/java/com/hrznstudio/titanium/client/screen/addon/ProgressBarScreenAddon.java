/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProgressBarScreenAddon<T extends IComponentHarness> extends BasicScreenAddon {

    private ProgressBarComponent<T> progressBar;
    private IAssetProvider provider;

    public ProgressBarScreenAddon(int posX, int posY, ProgressBarComponent<T> progressBarComponent) {
        super(posX, posY);
        this.progressBar = progressBarComponent;
    }

    @Override
    public int getXSize() {
        return (provider != null ? progressBar.getBarDirection().getXSize(provider) : 0);
    }

    @Override
    public int getYSize() {
        return (provider != null ? progressBar.getBarDirection().getYSize(provider) : 0);
    }

    @Override
    public void drawBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        this.provider = provider;
        progressBar.getBarDirection().render(screen, guiX, guiY, provider, this);
    }

    @Override
    public void drawForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {


    }

    public ProgressBarComponent<T> getProgressBar() {
        return progressBar;
    }

    @Override
    public List<String> getTooltipLines() {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(TextFormatting.GOLD + "Progress: " + TextFormatting.WHITE + new DecimalFormat().format(progressBar.getProgress()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(progressBar.getMaxProgress()));
        int progress = (progressBar.getMaxProgress() - progressBar.getProgress()) / progressBar.getProgressIncrease();
        if (!progressBar.getIncreaseType()) progress = progressBar.getMaxProgress() - progress;
        tooltip.add(TextFormatting.GOLD + "ETA: " + TextFormatting.WHITE + new DecimalFormat().format(Math.ceil(progress * progressBar.getTickingTime() / 20D)) + TextFormatting.DARK_AQUA + "s");
        return tooltip;
    }
}

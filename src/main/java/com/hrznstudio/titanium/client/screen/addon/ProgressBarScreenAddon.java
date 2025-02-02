/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        this.provider = provider;
        progressBar.getBarDirection().render(guiGraphics, screen, guiX, guiY, provider, this);
    }

    @Override
    public void drawForegroundLayer(GuiGraphics stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
    }

    public ProgressBarComponent<T> getProgressBar() {
        return progressBar;
    }

    @Override
    public List<Component> getTooltipLines() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(ChatFormatting.GOLD + Component.translatable("tooltip.titanium.progressbar.progress").getString() +  ChatFormatting.WHITE + new DecimalFormat().format(progressBar.getProgress()) + ChatFormatting.GOLD + "/" + ChatFormatting.WHITE + new DecimalFormat().format(progressBar.getMaxProgress())));
        int progress = (progressBar.getMaxProgress() - progressBar.getProgress());
        if (!progressBar.getIncreaseType()) progress = progressBar.getMaxProgress() - progress;
        tooltip.add(Component.literal(ChatFormatting.GOLD + Component.translatable("tooltip.titanium.eta").getString() + ChatFormatting.WHITE + new DecimalFormat().format(Math.ceil(progress * progressBar.getTickingTime() / 20D / progressBar.getProgressIncrease())) + ChatFormatting.DARK_AQUA + Component.translatable("tooltip.titanium.sec_short").getString()));
        return tooltip;
    }
}

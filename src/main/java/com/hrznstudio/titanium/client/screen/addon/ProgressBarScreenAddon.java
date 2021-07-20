/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

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
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        this.provider = provider;
        progressBar.getBarDirection().render(stack, screen, guiX, guiY, provider, this);
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {}

    public ProgressBarComponent<T> getProgressBar() {
        return progressBar;
    }

    @Override
    public List<ITextComponent> getTooltipLines() {
        List<ITextComponent> tooltip = new ArrayList<>();
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + new TranslationTextComponent("tooltip.titanium.progressbar.progress").getString() +  TextFormatting.WHITE + new DecimalFormat().format(progressBar.getProgress()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(progressBar.getMaxProgress())));
        int progress = (progressBar.getMaxProgress() - progressBar.getProgress());
        if (!progressBar.getIncreaseType()) progress = progressBar.getMaxProgress() - progress;
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + "ETA: " + TextFormatting.WHITE + new DecimalFormat().format(Math.ceil(progress * progressBar.getTickingTime() / 20D / progressBar.getProgressIncrease())) + TextFormatting.DARK_AQUA + "s"));
        return tooltip;
    }
}

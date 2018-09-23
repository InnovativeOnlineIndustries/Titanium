/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.client.gui.addon.ICanMouseDrag;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GuiAddonScreen extends GuiScreen {

    private IAssetProvider assetProvider;
    private int x;
    private int y;
    private List<IGuiAddon> addonList;
    private boolean drawBackground;

    private boolean isMouseDragging;
    private int dragX;
    private int dragY;

    public GuiAddonScreen(IAssetProvider assetProvider, boolean drawBackground) {
        this.assetProvider = assetProvider;
        this.drawBackground = drawBackground;
    }

    @Override
    public void initGui() {
        super.initGui();
        IBackgroundAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.x = this.width / 2 - background.getArea().width / 2;
        this.y = this.height / 2 - background.getArea().height / 2;
        this.addonList = this.guiAddons().stream().map(IFactory::create).collect(Collectors.toList());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.checkForMouseDrag(mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        if (drawBackground) {
            this.drawDefaultBackground();
            mc.getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND).getResourceLocation());
            drawTexturedModalRect(x, y, 0, 0, width, height);
        }
        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerBackgroundLayer(this, assetProvider, x, y, mouseX, mouseY, partialTicks));

        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerForegroundLayer(this, assetProvider, x, y, mouseX, mouseY));
        for (IGuiAddon iGuiAddon : addonList) {
            if (iGuiAddon.isInside(null, mouseX - x, mouseY - y) && !iGuiAddon.getTooltipLines().isEmpty()) {
                drawHoveringText(iGuiAddon.getTooltipLines(), mouseX - x, mouseY - y);
            }
        }
    }

    public abstract List<IFactory<IGuiAddon>> guiAddons();

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (Mouse.isButtonDown(0)) {
            if (!this.isMouseDragging) {
                this.isMouseDragging = true;
            } else {
                for (IGuiAddon iGuiAddon : this.addonList) {
                    if (iGuiAddon instanceof ICanMouseDrag && iGuiAddon.isInside(null, mouseX - x, mouseY - y)) {
                        ((ICanMouseDrag) iGuiAddon).drag(mouseX - dragX, mouseY - dragY);
                    }
                }
            }
            this.dragX = mouseX;
            this.dragY = mouseY;
        } else {
            this.isMouseDragging = false;
        }
    }
}

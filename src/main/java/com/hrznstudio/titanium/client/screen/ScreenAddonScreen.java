/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.client.screen.addon.interfaces.ICanMouseDrag;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ScreenAddonScreen extends Screen implements IScreenAddonConsumer {

    public int x;
    public int y;
    private IAssetProvider assetProvider;
    private List<IScreenAddon> addonList;
    private boolean drawBackground;

    private boolean isMouseDragging;
    private int dragX;
    private int dragY;

    public ScreenAddonScreen(IAssetProvider assetProvider, boolean drawBackground) {
        super(new StringTextComponent(""));
        this.assetProvider = assetProvider;
        this.drawBackground = drawBackground;
    }

    @Override
    public void init() {
        super.init();
        IBackgroundAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.x = this.width / 2 - background.getArea().width / 2;
        this.y = this.height / 2 - background.getArea().height / 2;
        this.addonList = this.guiAddons().stream().map(IFactory::create).collect(Collectors.toList());
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderSystem.pushMatrix();
        renderBackground(mouseX, mouseY, partialTicks);
        RenderSystem.popMatrix();
        super.render(mouseX, mouseY, partialTicks);
        RenderSystem.pushMatrix();
        renderForeground(mouseX, mouseY, partialTicks);
        RenderSystem.popMatrix();
    }

    public void renderBackground(int mouseX, int mouseY, float partialTicks) {
        this.checkForMouseDrag(mouseX, mouseY);
        RenderSystem.color4f(1, 1, 1, 1);
        if (drawBackground) {
            this.renderBackground();
            AssetUtil.drawAsset(this, assetProvider.getAsset(AssetTypes.BACKGROUND), x, y);
        }
        addonList.forEach(iGuiAddon -> iGuiAddon.drawBackgroundLayer(this, assetProvider, x, y, mouseX, mouseY, partialTicks));
    }

    public void renderForeground(int mouseX, int mouseY, float partialTicks) {
        addonList.forEach(iGuiAddon -> iGuiAddon.drawForegroundLayer(this, assetProvider, x, y, mouseX, mouseY));
        for (IScreenAddon iScreenAddon : addonList) {
            if (iScreenAddon.isInside(this, mouseX - x, mouseY - y) && !iScreenAddon.getTooltipLines().isEmpty()) {
                renderTooltip(iScreenAddon.getTooltipLines(), mouseX, mouseY);
            }
        }
    }

    public abstract List<IFactory<IScreenAddon>> guiAddons();

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {//Main Window
            if (!this.isMouseDragging) {
                this.isMouseDragging = true;
            } else {
                for (IScreenAddon iScreenAddon : this.addonList) {
                    if (iScreenAddon instanceof ICanMouseDrag && iScreenAddon.isInside(null, mouseX - x, mouseY - y)) {
                        ((ICanMouseDrag) iScreenAddon).drag(mouseX - dragX, mouseY - dragY);
                    }
                }
            }
            this.dragX = mouseX;
            this.dragY = mouseY;
        } else {
            this.isMouseDragging = false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        addonList.stream()
                .filter(iScreenAddon -> iScreenAddon instanceof IClickable && iScreenAddon.isInside(this, mouseX - x, mouseY - y))
                .forEach(iScreenAddon -> ((IClickable) iScreenAddon).handleClick(this, x, y, mouseX, mouseY, mouseButton));
        return false;
    }

    @Override
    public List<IScreenAddon> getAddons() {
        return addonList;
    }
}

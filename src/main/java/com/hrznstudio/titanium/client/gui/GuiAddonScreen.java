/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.client.gui.addon.ICanMouseDrag;
import com.hrznstudio.titanium.client.gui.addon.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GuiAddonScreen extends GuiScreen implements IGuiAddonConsumer {

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
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        this.checkForMouseDrag(mouseX, mouseY);
        GlStateManager.color4f(1, 1, 1, 1);
        if (drawBackground) {
            this.drawDefaultBackground();
            AssetUtil.drawAsset(this, assetProvider.getAsset(AssetTypes.BACKGROUND), x, y);
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
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        new ArrayList<>(addonList).stream().filter(iGuiAddon -> iGuiAddon instanceof IClickable && iGuiAddon.isInside(this, mouseX - x, mouseY - y))
                .forEach(iGuiAddon -> ((IClickable) iGuiAddon).handleClick(this, x, y, mouseX, mouseY, mouseButton));
        return false;
    }

    @Override
    public List<IGuiAddon> getAddons() {
        return addonList;
    }
}

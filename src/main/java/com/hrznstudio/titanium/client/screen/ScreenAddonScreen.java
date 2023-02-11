/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        super(Component.literal(""));
        this.assetProvider = assetProvider;
        this.drawBackground = drawBackground;
    }

    // init
    @Override
    public void init() {
        super.init();
        IBackgroundAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        // width
        this.x = this.width / 2 - background.getArea().width / 2;
        // height
        this.y = this.height / 2 - background.getArea().height / 2;
        this.addonList = this.guiAddons().stream().map(IFactory::create).collect(Collectors.toList());
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        matrixStack.pushPose();
        renderBackground(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pushPose();
        renderForeground(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }

    public void renderBackground(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.checkForMouseDrag(mouseX, mouseY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (drawBackground) {
            this.renderBackground(stack, 0);//draw tinted background
            AssetUtil.drawAsset(stack, this, assetProvider.getAsset(AssetTypes.BACKGROUND), x, y);
        }
        addonList.forEach(iGuiAddon -> iGuiAddon.drawBackgroundLayer(stack, this, assetProvider, x, y, mouseX, mouseY, partialTicks));
    }

    public void renderForeground(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        addonList.forEach(iGuiAddon -> iGuiAddon.drawForegroundLayer(stack, this, assetProvider, x, y, mouseX, mouseY, partialTicks));
        for (IScreenAddon iScreenAddon : addonList) {
            if (iScreenAddon.isMouseOver(mouseX - x, mouseY - y) && !iScreenAddon.getTooltipLines().isEmpty()) {
                // renderTooltip
                renderTooltip(stack, iScreenAddon.getTooltipLines(), Optional.empty(), mouseX, mouseY);
            }
        }
    }

    public abstract List<IFactory<IScreenAddon>> guiAddons();

    private void checkForMouseDrag(int mouseX, int mouseY) {
        int pressedButton = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT);
        if (pressedButton == GLFW.GLFW_PRESS) {//Main Window
            if (!this.isDragging()) {
                this.setDragging(true);
            } else {
                for (IScreenAddon iScreenAddon : this.addonList) {
                    if (iScreenAddon.isMouseOver(mouseX - x, mouseY - y)) {
                        iScreenAddon.mouseDragged(mouseX - x, mouseY - y, pressedButton, dragX, dragY);
                    }
                }
            }
            this.dragX = mouseX;
            this.dragY = mouseY;
        } else {
            this.setDragging(false);
        }
    }

    @Override
    public List<IScreenAddon> getAddons() {
        return addonList;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> children = new ArrayList<>(super.children());
        children.addAll(getAddons());
        return children;
    }
}

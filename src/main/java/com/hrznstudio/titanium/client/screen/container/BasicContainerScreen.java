/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.client.screen.IScreenAddonConsumer;
import com.hrznstudio.titanium.client.screen.addon.AssetScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.interfaces.ICanMouseDrag;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BasicContainerScreen<T extends Container> extends ContainerScreen<T> implements IScreenAddonConsumer {
    private final T container;
    private final ITextComponent title;
    private IAssetProvider assetProvider;
    private int xCenter;
    private int yCenter;
    private List<IScreenAddon> addons;

    private int dragX;
    private int dragY;
    private boolean isMouseDragging;

    public BasicContainerScreen(T container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.container = container;
        this.title = title;
        this.assetProvider = IAssetProvider.DEFAULT_PROVIDER;
        IAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.xSize = background.getArea().width;
        this.ySize = background.getArea().height;
        this.isMouseDragging = false;
        this.addons = new ArrayList<>();
    }

    public BasicContainerScreen(T container, PlayerInventory inventory, ITextComponent title, IAssetProvider provider) {
        super(container, inventory, title);
        this.container = container;
        this.title = title;
        this.assetProvider = provider;
        IAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.xSize = background.getArea().width;
        this.ySize = background.getArea().height;
        this.addons = new ArrayList<>();
    }

    // init
    @Override
    protected void init() {
        super.init();
        this.getAddons().forEach(screenAddon -> screenAddon.init(this.guiLeft, this.guiTop));
    }

    // drawGuiContainerBackgroundLayer
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        // renderBackground
        this.renderBackground(stack);
        // width
        xCenter = (width - xSize) / 2;
        // height
        yCenter = (height - ySize) / 2;
        //BG RENDERING
        RenderSystem.color4f(1, 1, 1, 1);
        getMinecraft().getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND).getResourceLocation());
        blit(stack, xCenter, yCenter, 0, 0, xSize, ySize);
        Minecraft.getInstance().fontRenderer.drawString(stack, TextFormatting.DARK_GRAY + title.getString(), xCenter + xSize / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(title.getString()) / 2, yCenter + 6, 0xFFFFFF);
        this.checkForMouseDrag(mouseX, mouseY);
        addons.forEach(iGuiAddon -> {
            if (iGuiAddon instanceof AssetScreenAddon) {
                AssetScreenAddon assetGuiAddon = (AssetScreenAddon) iGuiAddon;
                if (assetGuiAddon.isBackground()) {
                    iGuiAddon.drawBackgroundLayer(stack, this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks);
                }
            } else {
                iGuiAddon.drawBackgroundLayer(stack, this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks);
            }
        });
    }

    // drawGuiContainerForegroundLayer
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        addons.forEach(iGuiAddon -> {
            if (iGuiAddon instanceof AssetScreenAddon) {
                AssetScreenAddon assetGuiAddon = (AssetScreenAddon) iGuiAddon;
                if (assetGuiAddon.isBackground()) {
                    iGuiAddon.drawForegroundLayer(stack, this, assetProvider, xCenter, yCenter, mouseX, mouseY);
                }
            } else {
                iGuiAddon.drawForegroundLayer(stack, this, assetProvider, xCenter, yCenter, mouseX, mouseY);
            }
        });
        // renderHoveredToolTip
        renderHoveredTooltip(stack, mouseX - xCenter, mouseY - yCenter);
        for (IScreenAddon iScreenAddon : addons) {
            if (iScreenAddon.isInside(this, mouseX - xCenter, mouseY - yCenter) && !iScreenAddon.getTooltipLines().isEmpty()) {
                // renderTooltip
                func_243308_b(stack, iScreenAddon.getTooltipLines(), mouseX - xCenter, mouseY - yCenter);
            }
        }
    }

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {//Main Window
            if (!this.isMouseDragging) {
                this.isMouseDragging = true;
            } else {
                for (IScreenAddon iScreenAddon : this.addons) {
                    if (iScreenAddon instanceof ICanMouseDrag && iScreenAddon.isInside(null, mouseX - this.xCenter, mouseY - this.yCenter)) {
                        ((ICanMouseDrag) iScreenAddon).drag(mouseX - this.xCenter, mouseY - this.yCenter);
                    }
                }
            }
            this.dragX = mouseX;
            this.dragY = mouseY;
        } else {
            this.isMouseDragging = false;
        }
    }

    // mouseClicked
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        new ArrayList<>(addons).stream().filter(iGuiAddon -> iGuiAddon instanceof IClickable && iGuiAddon.isInside(this, mouseX - xCenter, mouseY - yCenter))
            .forEach(iGuiAddon -> ((IClickable) iGuiAddon).handleClick(this, xCenter, yCenter, mouseX, mouseY, mouseButton));
        return false;
    }

    // keyPressed
    @Override
    public boolean keyPressed(int keyCode, int scan, int modifiers) {
        return this.getAddons().stream()
            .anyMatch(screenAddon -> screenAddon.keyPressed(keyCode, scan, modifiers)) ||
            super.keyPressed(keyCode, scan, modifiers);
    }

    public int getX() {
        return xCenter;
    }

    public int getY() {
        return yCenter;
    }

    @Override
    @Nonnull
    public T getContainer() {
        return container;
    }

    @Override
    public List<IScreenAddon> getAddons() {
        return addons;
    }

    public void setAddons(List<IScreenAddon> addons) {
        this.addons = addons;
    }

    public IAssetProvider getAssetProvider() {
        return assetProvider;
    }

    public void setAssetProvider(IAssetProvider assetProvider) {
        this.assetProvider = assetProvider;
    }
}

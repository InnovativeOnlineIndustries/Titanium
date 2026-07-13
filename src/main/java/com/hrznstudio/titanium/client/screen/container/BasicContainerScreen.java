/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.client.screen.IScreenAddonConsumer;
import com.hrznstudio.titanium.client.screen.addon.AssetScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.WidgetScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IScreenAddonConsumer {
    private final T container;
    private final Component title;
    private IAssetProvider assetProvider;
    protected int xCenter;
    protected int yCenter;
    private List<IScreenAddon> addons;

    private int dragX;
    private int dragY;
    private boolean isMouseDragging;

    public BasicContainerScreen(T container, Inventory inventory, Component title) {
        super(container, inventory, title,
            IAssetProvider.getAsset(IAssetProvider.DEFAULT_PROVIDER, AssetTypes.BACKGROUND).getArea().width,
            IAssetProvider.getAsset(IAssetProvider.DEFAULT_PROVIDER, AssetTypes.BACKGROUND).getArea().height);
        this.container = container;
        this.title = title;
        this.assetProvider = IAssetProvider.DEFAULT_PROVIDER;
        this.isMouseDragging = false;
        this.addons = new ArrayList<>();
    }

    public BasicContainerScreen(T container, Inventory inventory, Component title, IAssetProvider provider) {
        super(container, inventory, title,
            IAssetProvider.getAsset(provider, AssetTypes.BACKGROUND).getArea().width,
            IAssetProvider.getAsset(provider, AssetTypes.BACKGROUND).getArea().height);
        this.container = container;
        this.title = title;
        this.assetProvider = provider;
        this.addons = new ArrayList<>();
    }

    // init
    @Override
    protected void init() {
        super.init();
        this.getAddons().forEach(screenAddon -> screenAddon.init(this.leftPos, this.topPos));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
        // Width
        xCenter = (width - imageWidth) / 2;
        // Height
        yCenter = (height - imageHeight) / 2;
        //BG RENDERING
        AssetUtil.blit(guiGraphics, IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND).getIdentifier(), xCenter, yCenter, 0, 0, imageWidth, imageHeight);
        guiGraphics.text(this.font, title.getString(), (int) getTitleX(xCenter), (int) getTitleY(yCenter), getTitleColor(), false);
        addons.stream().filter(IScreenAddon::isBackground).forEach(iGuiAddon -> {
            iGuiAddon.drawBackgroundLayer(guiGraphics, this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks);
        });
        addons.stream().filter(iScreenAddon -> !iScreenAddon.isBackground()).forEach(iGuiAddon -> {
            iGuiAddon.drawBackgroundLayer(guiGraphics, this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks);
        });
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        addons.forEach(iGuiAddon -> {
            if (iGuiAddon instanceof AssetScreenAddon assetGuiAddon) {
                if (!assetGuiAddon.isBackground()) {
                    iGuiAddon.drawForegroundLayer(guiGraphics, this, assetProvider, xCenter, yCenter, mouseX, mouseY, minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false));
                }
            } else {
                iGuiAddon.drawForegroundLayer(guiGraphics, this, assetProvider, xCenter, yCenter, mouseX, mouseY, minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            }
        });
        // renderHoveredToolTip
        for (IScreenAddon iScreenAddon : addons) {
            if (iScreenAddon.isMouseOver(mouseX, mouseY) && !iScreenAddon.getTooltipLines().isEmpty()) {
                guiGraphics.setComponentTooltipForNextFrame(this.font, iScreenAddon.getTooltipLines(), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.children() != null) {
            for (GuiEventListener listener : this.children()) {
                if (listener instanceof WidgetScreenAddon addon) {
                    AbstractWidget widget = addon.getWidget();
                    if (widget.keyPressed(event)) {
                        return true;
                    }
                    if (widget.isFocused()) {
                        if (event.scancode() == 18) {
                            return true;
                        }
                    }
                }
            }
        }
        return super.keyPressed(event);
    }

    private void checkForMouseDrag(int mouseX, int mouseY) {
        int pressedButton = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_LEFT);
        if (pressedButton == GLFW.GLFW_PRESS) {//Main Window
            if (!this.isMouseDragging) {
                this.isMouseDragging = true;
            } else {
                for (IScreenAddon iScreenAddon : this.addons) {
                    if (iScreenAddon.isMouseOver(mouseX, mouseY)) {
                        iScreenAddon.mouseDragged(mouseX - this.xCenter, mouseY - this.yCenter, pressedButton, dragX, dragY);
                    }
                }
            }
            this.dragX = mouseX;
            this.dragY = mouseY;
        } else {
            this.isMouseDragging = false;
        }
    }

    public int getX() {
        return xCenter;
    }

    public int getY() {
        return yCenter;
    }

    @Override
    @Nonnull
    public T getMenu() {
        return container;
    }

    @Override
    public List<IScreenAddon> getAddons() {
        return addons;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        if (super.children() != null) {
            List<GuiEventListener> collect = super.children().stream().map(guiEventListener -> (GuiEventListener) guiEventListener).collect(Collectors.toList());
            collect.addAll(getAddons());
            return collect;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return super.mouseScrolled(mouseX - xCenter, mouseY - yCenter, scrollX, scrollY);
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

    public int getTitleColor() {
        if (container instanceof BasicAddonContainer addonContainer) {
            return addonContainer.getTitleColorFromProvider();
        }
        return ChatFormatting.DARK_GRAY.getColor();
    }

    public float getTitleX(float xCenter) {
        if (container instanceof BasicAddonContainer addonContainer) {
            return addonContainer.getTitleXPos(font.width(title.getString()), width, height, imageWidth, imageHeight);
        }
        return xCenter / 2 + imageHeight / 2 - font.width(title.getString()) / 2;
    }

    public float getTitleY(float yCenter) {
        if (container instanceof BasicAddonContainer addonContainer) {
            return addonContainer.getTitleYPos(font.width(title.getString()), width, height, imageWidth, imageHeight);
        }
        return yCenter / 2 + 6;
    }
}

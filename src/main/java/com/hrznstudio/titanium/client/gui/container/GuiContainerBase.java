/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.container;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.IGuiAddonConsumer;
import com.hrznstudio.titanium.client.gui.addon.AssetGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.interfaces.ICanMouseDrag;
import com.hrznstudio.titanium.client.gui.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
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

public class GuiContainerBase<T extends Container> extends ContainerScreen<T> implements IGuiAddonConsumer {

    private final T container;
    private final ITextComponent title;
    private IAssetProvider assetProvider;
    private int xCenter;
    private int yCenter;
    private List<IGuiAddon> addons;

    private int dragX;
    private int dragY;

    public GuiContainerBase(T container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.container = container;
        this.title = title;
        this.assetProvider = IAssetProvider.DEFAULT_PROVIDER;
        IAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.xSize = background.getArea().width;
        this.ySize = background.getArea().height;
        this.addons = new ArrayList<>();
    }

    public GuiContainerBase(T container, PlayerInventory inventory, ITextComponent title, IAssetProvider provider) {
        super(container, inventory, title);
        this.container = container;
        this.title = title;
        this.assetProvider = provider;
        IAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.xSize = background.getArea().width;
        this.ySize = background.getArea().height;
        this.addons = new ArrayList<>();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        xCenter = (width - xSize) / 2;
        yCenter = (height - ySize) / 2;
        //BG RENDERING
        RenderSystem.color4f(1, 1, 1, 1);
        getMinecraft().getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND).getResourceLocation());
        blit(xCenter, yCenter, 0, 0, xSize, ySize);
        String name = TextFormatting.GRAY + title.getFormattedText();
        Minecraft.getInstance().fontRenderer.drawString(name, xCenter + xSize / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(name) / 2, yCenter + 6, 0xFFFFFF);
        this.checkForMouseDrag(mouseX, mouseY);
        addons.forEach(iGuiAddon -> {
            if (iGuiAddon instanceof AssetGuiAddon) {
                AssetGuiAddon assetGuiAddon = (AssetGuiAddon) iGuiAddon;
                if (assetGuiAddon.isBackground()) {
                    iGuiAddon.drawGuiContainerBackgroundLayer(this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks);
                }
            } else {
                iGuiAddon.drawGuiContainerBackgroundLayer(this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks);
            }
        });
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        addons.forEach(iGuiAddon -> {
            if (iGuiAddon instanceof AssetGuiAddon) {
                AssetGuiAddon assetGuiAddon = (AssetGuiAddon) iGuiAddon;
                if (assetGuiAddon.isBackground()) {
                    iGuiAddon.drawGuiContainerForegroundLayer(this, assetProvider, xCenter, yCenter, mouseX, mouseY);
                }
            } else {
                iGuiAddon.drawGuiContainerForegroundLayer(this, assetProvider, xCenter, yCenter, mouseX, mouseY);
            }
        });
        renderHoveredToolTip(mouseX - xCenter, mouseY - yCenter);
        for (IGuiAddon iGuiAddon : addons) {
            if (iGuiAddon.isInside(this, mouseX - xCenter, mouseY - yCenter) && !iGuiAddon.getTooltipLines().isEmpty()) {
                renderTooltip(iGuiAddon.getTooltipLines(), mouseX - xCenter, mouseY - yCenter);
            }
        }
    }

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().func_228018_at_().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) { //Main Windows
            for (IGuiAddon iGuiAddon : this.addons) {
                if (iGuiAddon instanceof ICanMouseDrag /*&& iGuiAddon.isInside(null, mouseX - x, mouseY - y)*/) {
                    ((ICanMouseDrag) iGuiAddon).drag(mouseX - dragX, mouseY - dragY);
                }
            }
        }
        this.dragX = mouseX;
        this.dragY = mouseY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        new ArrayList<>(addons).stream().filter(iGuiAddon -> iGuiAddon instanceof IClickable && iGuiAddon.isInside(this, mouseX - xCenter, mouseY - yCenter))
                .forEach(iGuiAddon -> ((IClickable) iGuiAddon).handleClick(this, xCenter, yCenter, mouseX, mouseY, mouseButton));
        return false;
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
    public List<IGuiAddon> getAddons() {
        return addons;
    }

    public void setAddons(List<IGuiAddon> addons) {
        this.addons = addons;
    }

    public IAssetProvider getAssetProvider() {
        return assetProvider;
    }

    public void setAssetProvider(IAssetProvider assetProvider) {
        this.assetProvider = assetProvider;
    }
}

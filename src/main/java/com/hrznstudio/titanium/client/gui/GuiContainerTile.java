/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.client.gui.addon.ICanMouseDrag;
import com.hrznstudio.titanium.client.gui.addon.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.container.ContainerTileBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerTile<T extends TileActive> extends ContainerScreen implements IGuiAddonConsumer, ITileContainer {

    private final ContainerTileBase<T> containerTileBase;
    private IAssetProvider assetProvider;
    private int x;
    private int y;
    private List<IGuiAddon> addonList;

    private boolean isMouseDragging;
    private int dragX;
    private int dragY;

    public GuiContainerTile(ContainerTileBase<T> containerTileBase) {
        super(containerTileBase);
        this.containerTileBase = containerTileBase;
        this.assetProvider = containerTileBase.getTile().getAssetProvider();
        IAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.xSize = background.getArea().width;
        this.ySize = background.getArea().height;
        this.addonList = new ArrayList<>();
        containerTileBase.getTile().getGuiAddons().forEach(factory -> addonList.add(factory.create()));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        //BG RENDERING
        GlStateManager.color4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND).getResourceLocation());
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        this.checkForMouseDrag(mouseX, mouseY);
        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerBackgroundLayer(this, assetProvider, x, y, mouseX, mouseY, partialTicks));
        containerTileBase.updateSlotPosition();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerForegroundLayer(this, assetProvider, x, y, mouseX, mouseY));
        renderHoveredToolTip(mouseX - x, mouseY - y);
        for (IGuiAddon iGuiAddon : addonList) {
            if (iGuiAddon.isInside(this, mouseX - x, mouseY - y) && !iGuiAddon.getTooltipLines().isEmpty()) {
                drawHoveringText(iGuiAddon.getTooltipLines(), mouseX - x, mouseY - y);
            }
        }
    }

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            this.isMouseDragging = true;
            for (IGuiAddon iGuiAddon : this.addonList) {
                if (iGuiAddon instanceof ICanMouseDrag /*&& iGuiAddon.isInside(null, mouseX - x, mouseY - y)*/) {
                    ((ICanMouseDrag) iGuiAddon).drag(mouseX - dragX, mouseY - dragY);
                }
            }
        } else {
            this.isMouseDragging = false;
        }
        this.dragX = mouseX;
        this.dragY = mouseY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        new ArrayList<>(addonList).stream().filter(iGuiAddon -> iGuiAddon instanceof IClickable && iGuiAddon.isInside(this, mouseX - x, mouseY - y))
                .forEach(iGuiAddon -> ((IClickable) iGuiAddon).handleClick(this, x, y, mouseX, mouseY, mouseButton));
        return false;
    }

    public IAssetProvider getAssetProvider() {
        return assetProvider;
    }

    public T getTile() {
        return containerTileBase.getTile();
    }

    public ContainerTileBase<T> getContainer() {
        return containerTileBase;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public List<IGuiAddon> getAddons() {
        return addonList;
    }

}

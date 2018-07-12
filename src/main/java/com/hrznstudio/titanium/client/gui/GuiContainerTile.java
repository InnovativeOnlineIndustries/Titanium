/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.container.ContainerTileBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerTile<T extends TileBase> extends GuiContainer {

    private final ContainerTileBase<T> containerTileBase;
    private IAssetProvider assetProvider;
    private int x;
    private int y;
    private List<IGuiAddon> addonList;

    public GuiContainerTile(ContainerTileBase<T> containerTileBase) {
        super(containerTileBase);
        this.containerTileBase = containerTileBase;
        this.assetProvider = containerTileBase.getTile().getAssetProvider();
        IAsset background = IAssetProvider.getAsset(assetProvider, IAssetProvider.AssetType.BACKGROUND);
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
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, IAssetProvider.AssetType.BACKGROUND).getResourceLocation());
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerBackgroundLayer(this, partialTicks, mouseX, mouseY));
        containerTileBase.updateSlotPosition();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerForegroundLayer(this, mouseX, mouseY));
        renderHoveredToolTip(mouseX - x, mouseY - y);
        for (IGuiAddon iGuiAddon : addonList) {
            if (iGuiAddon.isInside(this, mouseX - x, mouseY - y) && !iGuiAddon.getTooltipLines().isEmpty()) {
                drawHoveringText(iGuiAddon.getTooltipLines(), mouseX - x, mouseY - y);
            }
        }
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
}

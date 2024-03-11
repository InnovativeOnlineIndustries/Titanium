/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;

public class LockableOverlayAddon extends BasicScreenAddon{

    private SidedInventoryComponent component;
    private int xSize;
    private int ySize;

    public LockableOverlayAddon(SidedInventoryComponent inventoryComponent, int x, int y) {
        super(x, y);
        this.component = inventoryComponent;
        this.xSize = 0;
        this.ySize = 0;
    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        this.xSize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().width +2;
        this.ySize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().height;
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        if (isInside(screen, mouseX - guiX, mouseY - guiY)) {
            int extra = 4;
            IAsset asset = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER);
            Rectangle area = component.getRectangle(asset);
            AssetUtil.drawHorizontalLine(stack, area.x, area.x + area.width + extra, area.y, component.getColor());
            AssetUtil.drawHorizontalLine(stack, area.x, area.x + area.width + extra, area.y + area.height + extra, component.getColor());
            AssetUtil.drawVerticalLine(stack, area.x, area.y, area.y + area.height + extra, component.getColor());
            AssetUtil.drawVerticalLine(stack, area.x + area.width + extra, area.y , area.y + area.height + extra, component.getColor());
        }
    }

    @Override
    public int getXSize() {
        return this.xSize;
    }

    @Override
    public int getYSize() {
        return this.ySize;
    }
}

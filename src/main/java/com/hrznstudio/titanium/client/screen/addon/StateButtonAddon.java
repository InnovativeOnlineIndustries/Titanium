/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public abstract class StateButtonAddon extends BasicButtonAddon {

    private StateButtonInfo[] buttonInfos;

    public StateButtonAddon(ButtonComponent buttonComponent, StateButtonInfo... buttonInfos) {
        super(buttonComponent);
        this.buttonInfos = new StateButtonInfo[]{};
        if (buttonInfos != null) this.buttonInfos = buttonInfos;
    }


    @Override
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null) {
            //RenderSystem.setShaderColor(1, 1, 1, 1);
            AssetUtil.drawAsset(guiGraphics, screen, provider.getAsset(buttonInfo.getAsset()), this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public void drawForegroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null && isMouseOver(mouseX - guiX, mouseY - guiY)) {
            AssetUtil.drawSelectingOverlay(guiGraphics, getPosX() + 1, getPosY() + 1, getPosX() + getXSize() - 1, getPosY() + getYSize() - 1);
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null) {
            return Arrays.asList(buttonInfo.getTooltip());
        }
        return null;
    }

    public StateButtonInfo getStateInfo() {
        for (StateButtonInfo buttonInfo : buttonInfos) {
            if (buttonInfo.getState() == getState()) {
                return buttonInfo;
            }
        }
        return null;
    }

    public abstract int getState();
}

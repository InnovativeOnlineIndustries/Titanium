/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;

import java.util.Arrays;
import java.util.List;

public abstract class StateButtonAddon extends BasicButtonAddon {

    private StateButtonInfo[] buttonInfos;

    public StateButtonAddon(PosButton posButton, StateButtonInfo... buttonInfos) {
        super(posButton);
        this.buttonInfos = new StateButtonInfo[]{};
        if (buttonInfos != null) this.buttonInfos = buttonInfos;
    }


    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null) {
            GlStateManager.color4f(1, 1, 1, 1);
            AssetUtil.drawAsset(screen, provider.getAsset(buttonInfo.getAsset()), this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null && isInside(screen, mouseX, mouseY)) {
            AssetUtil.drawSelectingOverlay(getPosX() + 1, getPosY() + 1, getPosX() + getXSize() - 1, getPosY() + getYSize() - 1);
        }
    }

    @Override
    public List<String> getTooltipLines() {
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

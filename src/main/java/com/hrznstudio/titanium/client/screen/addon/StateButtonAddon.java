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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

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
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null) {
            RenderSystem.color4f(1, 1, 1, 1);
            AssetUtil.drawAsset(stack, screen, provider.getAsset(buttonInfo.getAsset()), this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null && isInside(screen, mouseX - guiX, mouseY - guiY)) {
            AssetUtil.drawSelectingOverlay(stack, getPosX() + 1, getPosY() + 1, getPosX() + getXSize() - 1, getPosY() + getYSize() - 1);
        }
    }

    @Override
    public List<ITextComponent> getTooltipLines() {
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

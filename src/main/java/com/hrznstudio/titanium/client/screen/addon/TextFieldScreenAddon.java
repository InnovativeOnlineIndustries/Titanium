/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class TextFieldScreenAddon extends BasicScreenAddon {
    private TextFieldWidget textFieldWidget;

    public TextFieldScreenAddon(int posX, int posY) {
        super(posX, posY);
        textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, posX,
                posY, 110, 16, new StringTextComponent(""));
    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        AssetUtil.drawAsset(stack, screen, this.getAsset(provider), this.getPosX() + guiX, this.getPosY() + guiY);
        // render
        textFieldWidget.renderButton(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {}

    @Override
    public void init(int guiX, int guiY) {
        String storage = textFieldWidget.getText();
        textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, guiX + this.getPosX() + 3,
                guiY + this.getPosY() + 4, 100, 16, new StringTextComponent(""));
        textFieldWidget.setEnableBackgroundDrawing(false);
        textFieldWidget.setText(storage);
    }

    @Override
    public boolean isInside(Screen screen, double mouseX, double mouseY) {
        return false;
    }

    @Override
    public int getXSize() {
        return textFieldWidget.getAdjustedWidth();
    }

    @Override
    public int getYSize() {
        return textFieldWidget.getHeight();
    }

    public void setActive(boolean active) {
        textFieldWidget.setEnabled(active);
    }

    @Override
    public boolean keyPressed(int key, int scan, int modifiers) {
        // keypressed
        return textFieldWidget.keyPressed(key, scan, modifiers) || textFieldWidget.canWrite();
    }

    private IAsset getAsset(IAssetProvider assetProvider) {
        return textFieldWidget.isEnabled() ? assetProvider.getAsset(AssetTypes.TEXT_FIELD_ACTIVE) :
                assetProvider.getAsset(AssetTypes.TEXT_FIELD_INACTIVE);
    }

    public IGuiEventListener getGuiListener() {
        return this.textFieldWidget;
    }

    public String getText() {
        return this.textFieldWidget.getText();
    }
}

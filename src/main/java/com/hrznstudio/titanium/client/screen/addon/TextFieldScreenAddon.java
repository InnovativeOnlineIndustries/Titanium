/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;



public class TextFieldScreenAddon extends BasicScreenAddon {
    private EditBox textFieldWidget;

    public TextFieldScreenAddon(int posX, int posY) {
        super(posX, posY);
        textFieldWidget = new EditBox(Minecraft.getInstance().font, posX,
                posY, 110, 16, new TextComponent(""));
    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        AssetUtil.drawAsset(stack, screen, this.getAsset(provider), this.getPosX() + guiX, this.getPosY() + guiY);
        // render
        textFieldWidget.renderButton(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {}

    @Override
    public void init(int guiX, int guiY) {
        String storage = textFieldWidget.getValue();
        textFieldWidget = new EditBox(Minecraft.getInstance().font, guiX + this.getPosX() + 3,
                guiY + this.getPosY() + 4, 100, 16, new TextComponent(""));
        textFieldWidget.setBordered(false);
        textFieldWidget.setValue(storage);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public int getXSize() {
        return textFieldWidget.getInnerWidth();
    }

    @Override
    public int getYSize() {
        return textFieldWidget.getHeight();
    }

    public void setActive(boolean active) {
        textFieldWidget.setEditable(active);
    }

    @Override
    public boolean keyPressed(int key, int scan, int modifiers) {
        // keypressed
        return textFieldWidget.keyPressed(key, scan, modifiers) || textFieldWidget.canConsumeInput();
    }

    private IAsset getAsset(IAssetProvider assetProvider) {
        return textFieldWidget.isEditable() ? assetProvider.getAsset(AssetTypes.TEXT_FIELD_ACTIVE) :
                assetProvider.getAsset(AssetTypes.TEXT_FIELD_INACTIVE);
    }

    public GuiEventListener getGuiListener() {
        return this.textFieldWidget;
    }

    public String getText() {
        return this.textFieldWidget.getValue();
    }
}

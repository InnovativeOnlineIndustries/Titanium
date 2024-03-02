/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;


public class WidgetScreenAddon extends BasicScreenAddon {

    private final AbstractWidget widget;

    public WidgetScreenAddon(int posX, int posY, AbstractWidget widget) {
        super(posX, posY);
        this.widget = widget;
    }

    @Override
    public void init(int screenX, int screenY) {
        this.widget.setX(screenX + getPosX());
        this.widget.setY(screenY + getPosY());
    }

    @Override
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void drawForegroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        widget.setX(getPosX());
        widget.setY(getPosY());
        widget.render(guiGraphics, mouseX, mouseY, partialTicks);
        widget.setX(guiX + getPosX());
        widget.setY(guiY + getPosY());
    }

    @Override
    public int getXSize() {
        return widget.getWidth();
    }

    @Override
    public int getYSize() {
        return widget.getHeight();
    }

    @Override
    public void mouseMoved(double xPos, double yPos) {
        widget.mouseMoved(xPos, yPos);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return widget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return widget.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return widget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return widget.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return widget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return widget.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return widget.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isFocused() {
        return widget.isFocused();
    }

    @Override
    public void setFocused(boolean focus) {
        widget.setFocused(focus);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX > this.getPosX() && mouseX < this.getPosX() + getXSize() && mouseY > this.getPosY() && mouseY < this.getPosY() + getYSize();
    }

    public AbstractWidget getWidget() {
        return widget;
    }
}

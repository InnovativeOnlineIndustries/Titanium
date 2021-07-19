package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;

public class WidgetScreenAddon extends BasicScreenAddon{

    private final Widget widget;

    public WidgetScreenAddon(int posX, int posY, Widget widget) {
        super(posX, posY);
        this.widget = widget;
    }

    @Override
    public void init(int screenX, int screenY) {
        Titanium.LOGGER.info("X:" + getPosX() + " Y:" + screenY);
        this.widget.x = screenX + getPosX();
        this.widget.y = screenY + getPosY();
    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {}

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        widget.x = getPosX();
        widget.y = getPosY();
        widget.render(stack, mouseX, mouseY, partialTicks);
        widget.x = guiX + getPosX();
        widget.y = guiY + getPosY();
    }

    @Override
    public int getXSize() {
        return widget.getWidth();
    }

    @Override
    public int getYSize() {
        return widget.getHeightRealms();
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
    public boolean handleKeyReleased(Screen screen, int keyCode, int scanCode, int modifiers) {
        return widget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return widget.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean handleCharTyped(Screen screen, char codePoint, int modifiers) {
        return widget.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return widget.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean changeFocus(boolean focus) {
        return widget.changeFocus(focus);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        boolean isOver = mouseX > this.getPosX() && mouseX < this.getPosX() + getXSize() && mouseY > this.getPosY() && mouseY < this.getPosY() + getYSize();
        //Titanium.LOGGER.info(isOver);
        return isOver;
    }

    public Widget getWidget() {
        return widget;
    }
}

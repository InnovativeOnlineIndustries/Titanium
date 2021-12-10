/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public interface IScreenAddon extends IGuiEventListener {
    /**
     * Draws the component in the background layer
     *
     * @param stack        The {@link com.mojang.blaze3d.matrix.MatrixStack}
     * @param screen       The current open screen
     * @param provider     The current asset provider used in the GUI
     * @param guiX         The gui X in the top left corner
     * @param guiY         The gui Y in the top left corner
     * @param mouseX       The current mouse X
     * @param mouseY       The current mouse Y
     * @param partialTicks Partial ticks
     */
    void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    /**
     * Draws the component in the foreground layer
     *
     * @param stack        The {@link com.mojang.blaze3d.matrix.MatrixStack}
     * @param screen       The current open screen
     * @param provider     The current asset provider used in the GUI
     * @param guiX         The gui X in the top left corner
     * @param guiY         The gui Y in the top left corner
     * @param mouseX       The current mouse X
     * @param mouseY       The current mouse Y
     * @param partialTicks Partial Ticks
     */
    void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY);
    void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    /**
     * A list of strings that will be drawn as a tooltip when `isInside` returns true
     *
     * @return Returns a list of tooltip lines represented as {@link net.minecraft.util.text.ITextComponent}'s
     */
    default List<Component> getTooltipLines() {
        return Collections.emptyList();
    }

    /**
     * Called when init is called in the screen.
     *
     * @param screenX the left point of the Screen
     * @param screenY the top point of the Screen
     */
    default void init(int screenX, int screenY) {}

    /**
     * @return Returns true if the addon is part of the background, background rendering.
     */
    default boolean isBackground() {
        return false;
    }

    /**
     * Called when the mouse is moved.
     *
     * @param xPos Current mouse coordinate X
     * @param yPos Current mouse coordinate Y
     */
    @Override
    default void mouseMoved(double xPos, double yPos) {}

    /**
     * Called when a mouse button is clicked
     *
     * @param mouseX The mouse X coordinate where it was clicked
     * @param mouseY The mouse Y coordinate where it was clicked
     * @param button The Id of the button that was clicked
     * @return Returns whether the mouse was clicked successfully or not
     */
    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Called when the mouse button is released
     *
     * @param mouseX The Mouse's X coordinate where it was released
     * @param mouseY The Mouse's Y coordinate where it was released
     * @param button The Id of the mouse button that was released
     * @return Returns true if the mouse button release was handled
     */
    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Called when the mouse is pressed and dragged
     *
     * @param mouseX The X coordinate of the mouse where the drag was initiated
     * @param mouseY The Y coordinate of the mouse where the drag was initiated
     * @param button The Id of the Button that was pressed
     * @param dragX The X coordinate of the mouse where the drag was finished
     * @param dragY The Y coordinate of the mouse where the drag was finished
     * @return Returns true if the drag was handled
     */
    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    /**
     * Called when mouse scroll is applied
     *
     * @param mouseX The X coordinate of the mouse where the scroll was initiated
     * @param mouseY The Y coordinate of the mouse where the scroll was initiated
     * @param delta The scroll wheel delta (Change rate)
     * @return Returns true if the scroll was handled
     */
    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    /**
     * Called when a key is pressed
     *
     * @param keyCode The keyboard key that was pressed or released
     * @param scanCode The system-specific scancode of the key
     * @param modifiers The 'bitfield' describing which modifiers keys were held down (ctrl, alt, shift, etc)
     * @return Returns true if the key press was handled
     */
    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Called when a key is pressed
     *
     * @param keyCode The keyboard key that was pressed or released
     * @param scanCode The system-specific scancode of the key
     * @param modifiers The 'bitfield' describing which modifiers keys were held down (ctrl, alt, shift, etc)
     * @return Returns whether the key release was handled
     */
    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Called when a specific 'Character' is typed
     *
     * @param codePoint The Unicode code point of the character
     * @param modifiers The 'bitfield' describing which modifiers keys were held down (ctrl, alt, shift, etc)
     * @return Returns true if the 'Character' being typed was handled
     */
    @Override
    default boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    /**
     * Called when Screen Focus changes
     *
     * @param focus If the screen the addon is part of is focused
     * @return Returns true if the widget is part of a focused screen
     */
    @Override
    default boolean changeFocus(boolean focus) {
        return false;
    }

    /**
     * Checks whether the mouse if over a specific object or point
     *
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     * @return Returns true if the mouse if over a specific object or point
     */
    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    // Titanium-Extension Methods - Most are just X -> X translations but Screen-Sensitive
    /**
     * Called when the mouse is moved.
     *
     * @param screen The current open screen
     * @param mouseX Current mouse coordinate X
     * @param mouseY Current mouse coordinate Y
     */
    default void handleMouseMoved(@Nullable Screen screen, double mouseX, double mouseY) {
        mouseMoved(mouseX, mouseY);
    }

    /**
     * Returns whether a specific portion of the screen was clicked
     *
     * @param screen The current open screen
     * @param guiX   The current GUI X position
     * @param guiY   The current GUI Y position
     * @param mouseX The current Mouse X position
     * @param mouseY The current Mouse Y position
     * @param mouseButton The current button being clicked
     * @return Returns whether the click was handled
     */
    default boolean handleMouseClicked(@Nullable Screen screen, int guiX, int guiY, double mouseX, double mouseY, int mouseButton) {
        return mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when the mouse button is released
     *
     * @param screen The current open screen
     * @param mouseX The Mouse's X coordinate where it was released
     * @param mouseY The Mouse's Y coordinate where it was released
     * @param button The Id of the mouse button that was released
     * @return Returns true if the mouse button release was handled
     */
    default boolean handleMouseReleased(@Nullable Screen screen, double mouseX, double mouseY, int button) {
        return mouseReleased(mouseX, button, button);
    }

    /**
     * Called when the mouse is pressed and dragged
     *
     * @param screen The current open screen
     * @param mouseX The X coordinate of the mouse where the drag was initiated
     * @param mouseY The Y coordinate of the mouse where the drag was initiated
     * @param button The Id of the Button that was pressed
     * @param dragX The X coordinate of the mouse where the drag was finished
     * @param dragY The Y coordinate of the mouse where the drag was finished
     * @return Returns true if the drag was handled
     */
    default boolean handleMouseDragged(@Nullable Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        return mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    /**
     * Called when mouse scroll is applied
     *
     * @param screen The current open screen
     * @param mouseX The X coordinate of the mouse where the scroll was initiated
     * @param mouseY The Y coordinate of the mouse where the scroll was initiated
     * @param delta The scroll wheel delta (Change rate)
     * @return Returns true if the scroll was handled
     */
    default boolean handleMouseScrolled(@Nullable Screen screen, double mouseX, double mouseY, double delta) {
        return mouseScrolled(mouseX, mouseY, delta);
    }

    /**
     * Called when a key is pressed
     *
     * @param screen The current open screen
     * @param keyCode The keyboard key that was pressed or released
     * @param scanCode The system-specific scancode of the key
     * @param modifiers The 'bitfield' describing which modifiers keys were held down (ctrl, alt, shift, etc)
     * @return Returns true if the key press was handled
     */
    default boolean handleKeyPressed(@Nullable Screen screen, int keyCode, int scanCode, int modifiers) {
        return keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Called when a key is pressed
     *
     * @param screen The current open screen
     * @param keyCode The keyboard key that was pressed or released
     * @param scanCode The system-specific scancode of the key
     * @param modifiers The 'bitfield' describing which modifiers keys were held down (ctrl, alt, shift, etc)
     * @return Returns whether the key release was handled
     */
    default boolean handleKeyReleased(@Nullable Screen screen, int keyCode, int scanCode, int modifiers) {
        return keyReleased(keyCode, scanCode, modifiers);
    }

    /**
     * Called when a specific 'Character' is typed
     *
     * @param screen The current open screen
     * @param codePoint The Unicode code point of the character
     * @param modifiers The 'bitfield' describing which modifiers keys were held down (ctrl, alt, shift, etc)
     * @return Returns true if the 'Character' being typed was handled
     */
    default boolean handleCharTyped(@Nullable Screen screen, char codePoint, int modifiers) {
        return charTyped(codePoint, modifiers);
    }

    /**
     * Called when Screen Focus changes
     *
     * @param screen The current open screen
     * @param focus If the screen the addon is part of is focused
     * @return Returns true if the widget is part of a focused screen
     */
    default boolean handleFocusChange(@Nullable Screen screen, boolean focus) {
        return changeFocus(focus);
    }

    /**
     * A check to know if the mouse is inside of the component to draw the tooltip lines
     *
     * @param screen The current open screen
     * @param mouseX The current mouse X
     * @param mouseY The current mouse Y
     * @return Returns true if the mouse is inside the component
     */
    default boolean isInside(@Nullable Screen screen, double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY);
    }
}

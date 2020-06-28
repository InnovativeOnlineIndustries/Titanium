/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextProperties;

import java.util.Collections;
import java.util.List;

public class BasicButtonAddon extends BasicScreenAddon implements IClickable {

    private ButtonComponent button;

    public BasicButtonAddon(ButtonComponent buttonComponent) {
        super(buttonComponent.getPosX(), buttonComponent.getPosY());
        this.button = buttonComponent;
    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {}

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {}

    @Override
    public List<ITextProperties> getTooltipLines() {
        return Collections.emptyList();
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f, Minecraft.getInstance().player.func_233580_cy_())); //getPosition
        if (screen instanceof ContainerScreen && ((ContainerScreen) screen).getContainer() instanceof ILocatable) {
            ILocatable locatable = (ILocatable) ((ContainerScreen) screen).getContainer();
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), this.button.getId(), new CompoundNBT()));
        }
    }

    @Override
    public int getXSize() {
        return button.getSizeX();
    }

    @Override
    public int getYSize() {
        return button.getSizeY();
    }

    public ButtonComponent getButton() {
        return button;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.block.tile.button.PosButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public abstract class BasicButtonAddon extends BasicGuiAddon implements IClickable {

    private PosButton button;

    public BasicButtonAddon(PosButton posButton) {
        super(posButton.getPosX(), posButton.getPosY());
        this.button = posButton;
    }

    @Override
    public List<String> getTooltipLines() {
        return null;
    }

    @Override
    public void handleClick(GuiScreen tile, int guiX, int guiY, int mouseX, int mouseY, int button) {
        //Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F)); TODO
        //        Litterboxlib.NETWORK.sendToServer(new ButtonClickedMessage(information.getContainerTile().getTile().getPos(), this.button.getId()));
    }

    @Override
    public int getXSize() {
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;

public class BasicButtonAddon extends BasicGuiAddon implements IClickable {

    private PosButton button;

    public BasicButtonAddon(PosButton posButton) {
        super(posButton.getPosX(), posButton.getPosY());
        this.button = posButton;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipLines() {
        return null;
    }

    @Override
    public void handleClick(GuiScreen tile, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f, Minecraft.getInstance().player.getPosition()));
        if (tile instanceof GuiContainerTile)
            NetworkHandler.NETWORK.sendToServer(new ButtonClickNetworkMessage(((GuiContainerTile) tile).getContainer().getTile().getPos(), this.button.getId(), new NBTTagCompound()));
    }

    @Override
    public int getXSize() {
        return button.getSizeX();
    }

    @Override
    public int getYSize() {
        return button.getSizeY();
    }

    public static class ButtonClickNetworkMessage extends Message {

        private BlockPos pos;
        private int id;
        private NBTTagCompound data;

        public ButtonClickNetworkMessage(BlockPos pos, int id, NBTTagCompound data) {
            this.pos = pos;
            this.id = id;
            this.data = data;
        }

        public ButtonClickNetworkMessage() {

        }

        @Override
        protected void handleMessage(NetworkEvent.Context context) {
            TileUtil.getTileEntity(context.getSender().world, pos, TileActive.class).ifPresent(tileActive -> {
                if (tileActive.getMultiButtonHandler() != null)
                    tileActive.getMultiButtonHandler().clickButton(id, data);
            });
        }
    }
}

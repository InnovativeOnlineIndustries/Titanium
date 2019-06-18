/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.container.ContainerTileBase;
import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipLines() {
        return null;
    }

    @Override
    public void handleClick(Screen tile, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f, Minecraft.getInstance().player.getPosition()));
        if (tile instanceof IHasContainer && ((IHasContainer) tile).getContainer() instanceof ContainerTileBase)
            NetworkHandler.NETWORK.sendToServer(new ButtonClickNetworkMessage(((ContainerTileBase) ((IHasContainer) tile).getContainer()).getTile().getPos(), this.button.getId(), new CompoundNBT()));
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
        private CompoundNBT data;

        public ButtonClickNetworkMessage(BlockPos pos, int id, CompoundNBT data) {
            this.pos = pos;
            this.id = id;
            this.data = data;
        }

        public ButtonClickNetworkMessage() {

        }

        @Override
        protected void handleMessage(NetworkEvent.Context context) {
            TileUtil.getTileEntity(context.getSender().world, pos, TileActive.class).ifPresent(tileActive -> {
                tileActive.handleButtonMessage(id, data);
            });
        }
    }
}

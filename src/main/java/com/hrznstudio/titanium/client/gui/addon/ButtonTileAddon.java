package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.client.gui.ITileContainer;
import com.hrznstudio.titanium.container.impl.ContainerTileBase;
import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class ButtonTileAddon extends BasicButtonAddon {
    public ButtonTileAddon(PosButton posButton) {
        super(posButton);
    }

    @Override
    public void handleClick(Screen tile, int guiX, int guiY, double mouseX, double mouseY, int button) {
        super.handleClick(tile, guiX, guiY, mouseX, mouseY, button);
        if (tile instanceof IHasContainer && ((IHasContainer) tile).getContainer() instanceof ContainerTileBase)
            NetworkHandler.NETWORK.sendToServer(new ButtonClickNetworkMessage(((ContainerTileBase) ((IHasContainer) tile).getContainer()).getTile().getPos(), getButton().getId(), new CompoundNBT()));
        if (tile instanceof ITileContainer) {
            NetworkHandler.NETWORK.sendToServer(new ButtonClickNetworkMessage(((ITileContainer) tile).getTile().getPos(), getButton().getId(), new CompoundNBT()));
        }
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
                tileActive.handleButtonMessage(id, context.getSender(), data);
            });
        }
    }
}

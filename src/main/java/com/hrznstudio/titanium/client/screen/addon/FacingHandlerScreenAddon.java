/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.block.tile.sideness.SidedHandlerManager;
import com.hrznstudio.titanium.client.screen.IScreenAddonConsumer;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.client.screen.container.BasicContainerTileScreen;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.hrznstudio.titanium.util.FacingUtil;
import com.hrznstudio.titanium.util.LangUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FacingHandlerScreenAddon extends BasicScreenAddon implements IClickable {

    private final IFacingHandler handler;
    private final SidedHandlerManager manager;
    private List<StateButtonAddon> buttonAddons;
    private int xSize;
    private int ySize;
    private boolean clicked;
    private Point inventoryPoint;
    private Point hotbarPoint;
    private IAssetType assetType;

    public FacingHandlerScreenAddon(SidedHandlerManager manager, IFacingHandler facingHandler, IAssetType assetType) {
        super(manager.getPosX(), manager.getPosY());
        this.manager = manager;
        this.handler = facingHandler;
        this.buttonAddons = new ArrayList<>();
        this.xSize = 0;
        this.ySize = 0;
        this.clicked = false;
        this.assetType = assetType;
    }

    public static Point getPointFromFacing(FacingUtil.Sideness sideness, Point inventory) {
        Point origin = new Point(inventory.x + 73, inventory.y + 19);
        switch (sideness) {
            case TOP:
                origin.translate(0, -16);
                break;
            case BOTTOM:
                origin.translate(0, 16);
                break;
            case LEFT:
                origin.translate(-16, 0);
                break;
            case RIGHT:
                origin.translate(16, 0);
                break;
            case BACK:
                origin.translate(16, 16);
                break;
        }
        return origin;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        IBackgroundAsset backgroundInfo = provider.getAsset(AssetTypes.BACKGROUND);
        inventoryPoint = backgroundInfo.getInventoryPosition();
        hotbarPoint = backgroundInfo.getHotbarPosition();
        this.xSize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().width;
        this.ySize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().height;
        GlStateManager.color4f(1, 1, 1, 1);
        AssetUtil.drawAsset(screen, provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER), guiX + getPosX(), guiY + getPosY());
        int offset = 2;
        AbstractGui.fill(guiX + getPosX() + offset, guiY + getPosY() + offset, guiX + getPosX() + getXSize() - offset, guiY + getPosY() + getYSize() - offset, handler.getColor());
        GlStateManager.color4f(1, 1, 1, 1);
        if (isClicked()) {
            //draw the overlay for the slots
            screen.blit(guiX + backgroundInfo.getInventoryPosition().x - 1, guiY + backgroundInfo.getInventoryPosition().y - 1, 16, 213 + 18, 14, 14);
            screen.blit(guiX + backgroundInfo.getInventoryPosition().x - 1, guiY + backgroundInfo.getInventoryPosition().y - 1, 56, 185, 162, 54);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        if (isInside(screen, mouseX - guiX, mouseY - guiY) || isClicked()) {
            IAsset asset = provider.getAsset(assetType);
            Rectangle area = handler.getRectangle(asset);
            AssetUtil.drawHorizontalLine(area.x, area.x + area.width, area.y, handler.getColor());
            AssetUtil.drawHorizontalLine(area.x, area.x + area.width, area.y + area.height, handler.getColor());
            AssetUtil.drawVerticalLine(area.x, area.y, area.y + area.height, handler.getColor());
            AssetUtil.drawVerticalLine(area.x + area.width, area.y, area.y + area.height, handler.getColor());
        }
    }

    @Override
    public List<String> getTooltipLines() {
        return Arrays.asList(LangUtil.get("tooltip.titanium.facing_handler." + handler.getName().toLowerCase()));
    }

    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return ySize;
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        if (screen instanceof BasicContainerTileScreen) {
            for (IScreenAddon addon : new ArrayList<>(((IScreenAddonConsumer) screen).getAddons())) {
                if (addon instanceof FacingHandlerScreenAddon && addon != this) {
                    ((FacingHandlerScreenAddon) addon).setClicked((BasicContainerTileScreen) screen, false);
                    ((BasicContainerTileScreen) screen).getContainer().setDisabled(true);
                }
            }
            this.setClicked((BasicContainerTileScreen) screen, !clicked);
            if (clicked) {
                ((BasicContainerTileScreen) screen).getContainer().setDisabled(true);
                for (FacingUtil.Sideness facing : FacingUtil.Sideness.values()) {
                    if (!handler.getFacingModes().containsKey(facing)) continue;
                    Point point = getPointFromFacing(facing, inventoryPoint);
                    StateButtonAddon addon = new StateButtonAddon(new PosButton(point.x, point.y, 14, 14), IFacingHandler.FaceMode.NONE.getInfo(), IFacingHandler.FaceMode.ENABLED.getInfo(), IFacingHandler.FaceMode.PULL.getInfo(), IFacingHandler.FaceMode.PUSH.getInfo()) {
                        @Override
                        public int getState() {
                            IFacingHandler handler = ((BasicContainerTileScreen) screen).getContainer().getTile().getHandlerFromName(FacingHandlerScreenAddon.this.handler.getName());
                            return handler != null && handler.getFacingModes().containsKey(facing) ? handler.getFacingModes().get(facing).getIndex() : 0;
                        }

                        @Override
                        public void handleClick(Screen gui, int guiX, int guiY, double mouseX, double mouseY, int mouse) {
                            StateButtonInfo info = getStateInfo();
                            if (info != null && gui instanceof BasicContainerTileScreen) {
                                CompoundNBT compound = new CompoundNBT();
                                compound.putString("Facing", facing.name());
                                int faceMode = (getState() + (mouse == 0 ? 1 : -1)) % IFacingHandler.FaceMode.values().length;
                                if (faceMode < 0) faceMode = IFacingHandler.FaceMode.values().length - 1;
                                compound.putInt("Next", faceMode);
                                compound.putString("Name", handler.getName());
                                Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(((BasicContainerTileScreen) gui).getContainer().getLocatorInstance(), -1, compound));
                                handler.getFacingModes().put(facing, IFacingHandler.FaceMode.values()[faceMode]);
                                ((BasicContainerTileScreen) gui).getContainer().getTile().updateNeigh();
                            }
                        }

                        @Override
                        public List<String> getTooltipLines() {
                            List<String> strings = new ArrayList<>();
                            IFacingHandler.FaceMode mode = IFacingHandler.FaceMode.values()[getState()];
                            strings.add(TextFormatting.GOLD + LangUtil.get("tooltip.titanium.facing_handler.direction") +
                                    TextFormatting.RESET + LangUtil.get("tooltip.titanium.facing_handler." + facing.name().toLowerCase()));
                            strings.add(TextFormatting.GOLD + LangUtil.get("tooltip.titanium.facing_handler.action") +
                                    mode.getColor() + LangUtil.get("tooltip.titanium.facing_handler." + getStateInfo().getTooltip()[0]));
                            return strings;
                        }
                    };
                    buttonAddons.add(addon);
                    ((BasicContainerTileScreen) screen).getAddons().add(addon);
                }
            }
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(BasicContainerTileScreen information, boolean clicked) {
        this.clicked = clicked;
        if (!clicked) {
            information.getAddons().removeIf(iGuiAddon -> buttonAddons.contains(iGuiAddon));
            buttonAddons.clear();
            information.getContainer().setDisabled(false);
        }
    }

}

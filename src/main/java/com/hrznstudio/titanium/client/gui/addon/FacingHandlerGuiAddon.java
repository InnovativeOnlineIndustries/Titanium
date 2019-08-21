/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.block.tile.sideness.SidedHandlerManager;
import com.hrznstudio.titanium.client.gui.IGuiAddonConsumer;
import com.hrznstudio.titanium.client.gui.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.client.gui.container.GuiContainerTileBase;
import com.hrznstudio.titanium.network.NetworkHandler;
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


public class FacingHandlerGuiAddon extends BasicGuiAddon implements IClickable {

    private final IFacingHandler handler;
    private final SidedHandlerManager manager;
    private List<StateButtonAddon> buttonAddons;
    private int xSize;
    private int ySize;
    private boolean clicked;

    public FacingHandlerGuiAddon(SidedHandlerManager manager, IFacingHandler facingHandler) {
        super(manager.getPosX(), manager.getPosY());
        this.manager = manager;
        this.handler = facingHandler;
        this.buttonAddons = new ArrayList<>();
        this.xSize = 0;
        this.ySize = 0;
        this.clicked = false;
    }

    public static Point getPointFromFacing(FacingUtil.Sideness sideness) {
        Point origin = new Point(32, 121);
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
        this.xSize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().width;
        this.ySize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().height;
        GlStateManager.color4f(1, 1, 1, 1);
        AssetUtil.drawAsset(screen, provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER), guiX + getPosX(), guiY + getPosY());
        int offset = 2;
        AbstractGui.fill(guiX + getPosX() + offset, guiY + getPosY() + offset, guiX + getPosX() + getXSize() - offset, guiY + getPosY() + getYSize() - offset, handler.getColor());
        GlStateManager.color4f(1, 1, 1, 1);
        if (isClicked()) {
            //TODO draw the overlay for the slots
            screen.blit(guiX + getPosX(), guiY + getPosY(), 16, 213 + 18, 14, 14);
            screen.blit(guiX + 7, guiY + 101, 56, 185, 162, 54);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        if (isInside(screen, mouseX - guiX, mouseY - guiY) || isClicked()) {
            AssetUtil.drawHorizontalLine(handler.getRectangle().x, handler.getRectangle().x + handler.getRectangle().width, handler.getRectangle().y, handler.getColor());
            AssetUtil.drawHorizontalLine(handler.getRectangle().x, handler.getRectangle().x + handler.getRectangle().width, handler.getRectangle().y + handler.getRectangle().height, handler.getColor());
            AssetUtil.drawVerticalLine(handler.getRectangle().x, handler.getRectangle().y, handler.getRectangle().y + handler.getRectangle().height, handler.getColor());
            AssetUtil.drawVerticalLine(handler.getRectangle().x + handler.getRectangle().width, handler.getRectangle().y, handler.getRectangle().y + handler.getRectangle().height, handler.getColor());
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
        if (screen instanceof GuiContainerTileBase) {
            for (IGuiAddon addon : new ArrayList<>(((IGuiAddonConsumer) screen).getAddons())) {
                if (addon instanceof FacingHandlerGuiAddon && addon != this) {
                    ((FacingHandlerGuiAddon) addon).setClicked((GuiContainerTileBase) screen, false);
                }
            }
            this.setClicked((GuiContainerTileBase) screen, !clicked);
            if (clicked) {
                ((GuiContainerTileBase) screen).getContainer().removeChestInventory();
                for (FacingUtil.Sideness facing : FacingUtil.Sideness.values()) {
                    if (!handler.getFacingModes().containsKey(facing)) continue;
                    Point point = getPointFromFacing(facing);
                    StateButtonAddon addon = new StateButtonAddon(new PosButton(point.x, point.y, 14, 14), IFacingHandler.FaceMode.NONE.getInfo(), IFacingHandler.FaceMode.ENABLED.getInfo(), IFacingHandler.FaceMode.PULL.getInfo(), IFacingHandler.FaceMode.PUSH.getInfo()) {
                        @Override
                        public int getState() {
                            IFacingHandler handler = ((GuiContainerTileBase) screen).getContainer().getTile().getHandlerFromName(FacingHandlerGuiAddon.this.handler.getName());
                            return handler != null && handler.getFacingModes().containsKey(facing) ? handler.getFacingModes().get(facing).getIndex() : 0;
                        }

                        @Override
                        public void handleClick(Screen gui, int guiX, int guiY, double mouseX, double mouseY, int mouse) {
                            StateButtonInfo info = getStateInfo();
                            if (info != null && gui instanceof GuiContainerTileBase) {
                                CompoundNBT compound = new CompoundNBT();
                                compound.putString("Facing", facing.name());
                                int faceMode = (getState() + (mouse == 0 ? 1 : -1)) % IFacingHandler.FaceMode.values().length;
                                if (faceMode < 0) faceMode = IFacingHandler.FaceMode.values().length - 1;
                                compound.putInt("Next", faceMode);
                                compound.putString("Name", handler.getName());
                                NetworkHandler.NETWORK.sendToServer(new ButtonClickNetworkMessage(((GuiContainerTileBase) gui).getContainer().getTile().getPos(), -1, compound));
                                handler.getFacingModes().put(facing, IFacingHandler.FaceMode.values()[faceMode]);
                                ((GuiContainerTileBase) gui).getContainer().getTile().updateNeigh();
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
                    ((GuiContainerTileBase) screen).getAddons().add(addon);
                }
            }
        }

    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(GuiContainerTileBase information, boolean clicked) {
        this.clicked = clicked;
        if (!clicked) {
            information.getContainer().addPlayerChestInventory();
            information.getAddons().removeIf(iGuiAddon -> buttonAddons.contains(iGuiAddon));
            buttonAddons.clear();
        }
    }


}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.IGuiAddonConsumer;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FacingHandlerGuiAddon extends BasicGuiAddon implements IClickable {

    private final IFacingHandler handler;
    private final Rectangle location;
    private boolean clicked;
    private List<StateButtonAddon> buttonAddons;

    public FacingHandlerGuiAddon(Rectangle location, IFacingHandler facingHandler) {
        super(location.x, location.y);
        this.location = location;
        this.handler = facingHandler;
        this.buttonAddons = new ArrayList<>();
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
    public void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color4f(1, 1, 1, 1);
        //TODO draw the button highlight overlay
//        Minecraft.getMinecraft().getTextureManager().bindTexture(BG_TEXTURE);
//        container.drawTexturedModalRect(container.getGuiLeft() + getPosX(), container.getGuiTop() + getPosY(), 1, 213 + 18, 14, 14);
        int offset = 2;
        GuiScreen.drawRect(guiX + getPosX() + offset, guiY + getPosY() + offset, getXSize() - offset - 2, getYSize() - offset - 2, handler.getColor());
        if (isClicked()) {
            //TODO draw the overlay for the slots
//            container.drawTexturedModalRect(container.getGuiLeft() + getPosX(), container.getGuiTop() + getPosY(), 16, 213 + 18, 14, 14);
//            container.drawTexturedModalRect(container.getGuiLeft() + 7, container.getGuiTop() + 101, 56, 185, 162, 54);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        if (isInside(screen, mouseX, mouseY) || isClicked()) {
            AssetUtil.drawHorizontalLine(handler.getRectangle().x, handler.getRectangle().x + handler.getRectangle().width, handler.getRectangle().y, handler.getColor());
            AssetUtil.drawHorizontalLine(handler.getRectangle().x, handler.getRectangle().x + handler.getRectangle().width, handler.getRectangle().y + handler.getRectangle().height, handler.getColor());
            AssetUtil.drawVerticalLine(handler.getRectangle().x, handler.getRectangle().y, handler.getRectangle().y + handler.getRectangle().height, handler.getColor());
            AssetUtil.drawVerticalLine(handler.getRectangle().x + handler.getRectangle().width, handler.getRectangle().y, handler.getRectangle().y + handler.getRectangle().height, handler.getColor());
        }
    }

    @Override
    public List<String> getTooltipLines() {
        return Arrays.asList(handler.getName());
    }

    @Override
    public int getXSize() {
        return location.width;
    }

    @Override
    public int getYSize() {
        return location.height;
    }

    @Override
    public void handleClick(GuiScreen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        if (screen instanceof GuiContainerTile) {
            for (IGuiAddon addon : new ArrayList<>(((IGuiAddonConsumer) screen).getAddons())) {
                if (addon instanceof FacingHandlerGuiAddon && addon != this) {
                    ((FacingHandlerGuiAddon) addon).setClicked((GuiContainerTile) screen, false);
                }
            }
            this.setClicked((GuiContainerTile) screen, !clicked);
            if (clicked) {
                ((GuiContainerTile) screen).getContainer().removeChestInventory();
                EnumFacing relative = ((GuiContainerTile) screen).getContainer().getTile().getFacingDirection();
                for (EnumFacing facing : EnumFacing.values()) {
                    if (!handler.getFacingModes().containsKey(facing)) continue;
                    FacingUtil.Sideness sideness = FacingUtil.getFacingRelative(relative, facing);
                    Point point = getPointFromFacing(sideness);
                    StateButtonAddon addon = new StateButtonAddon(new PosButton(point.x, point.y, 14, 14), IFacingHandler.FaceMode.NONE.getInfo(), IFacingHandler.FaceMode.ENABLED.getInfo(), IFacingHandler.FaceMode.PULL.getInfo(), IFacingHandler.FaceMode.PUSH.getInfo()) {
                        @Override
                        public int getState() {
                            IFacingHandler handler = ((GuiContainerTile) screen).getContainer().getTile().getHandlerFromName(FacingHandlerGuiAddon.this.handler.getName());
                            return handler != null && handler.getFacingModes().containsKey(facing) ? handler.getFacingModes().get(facing).getIndex() : 0;
                        }

                        @Override
                        public void handleClick(GuiScreen gui, int guiX, int guiY, double mouseX, double mouseY, int mouse) {
                            StateButtonInfo info = getStateInfo();
                            if (info != null && gui instanceof GuiContainerTile) {
                                NBTTagCompound compound = new NBTTagCompound();
                                compound.putString("Facing", facing.getName());
                                int faceMode = (getState() + (mouse == 0 ? 1 : -1)) % IFacingHandler.FaceMode.values().length;
                                if (faceMode < 0) faceMode = IFacingHandler.FaceMode.values().length - 1;
                                compound.putInt("Next", faceMode);
                                compound.putString("Name", handler.getName());
                                //Litterboxlib.NETWORK.sendToServer(new TileUpdateFromClientMessage("SIDE_CHANGE", gui.getContainerTile().getTile().getPos(), compound)); TODO
                                handler.getFacingModes().put(facing, IFacingHandler.FaceMode.values()[faceMode]);
                                ((GuiContainerTile) gui).getContainer().getTile().updateNeigh();
                            }
                        }

                        @Override
                        public List<String> getTooltipLines() {
                            List<String> strings = new ArrayList<>(super.getTooltipLines());
                            strings.add(sideness.toString());
                            return strings;
                        }
                    };
                    buttonAddons.add(addon);
                    ((GuiContainerTile) screen).getAddons().add(addon);
                }
            }
        }

    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(GuiContainerTile information, boolean clicked) {
        this.clicked = clicked;
        if (!clicked) {
            information.getContainer().addPlayerChestInventory();
            information.getAddons().removeIf(iGuiAddon -> buttonAddons.contains(iGuiAddon));
            buttonAddons.clear();
        }
    }


}

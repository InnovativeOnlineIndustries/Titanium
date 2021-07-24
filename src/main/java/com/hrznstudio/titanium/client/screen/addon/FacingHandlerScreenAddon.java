/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
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
import com.hrznstudio.titanium.client.screen.IScreenAddonConsumer;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponentHarness;
import com.hrznstudio.titanium.component.sideness.SidedComponentManager;
import com.hrznstudio.titanium.container.IDisableableContainer;
import com.hrznstudio.titanium.container.IObjectContainer;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.hrznstudio.titanium.util.FacingUtil;
import com.hrznstudio.titanium.util.LangUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class FacingHandlerScreenAddon extends BasicScreenAddon implements IClickable {

    private final IFacingComponent handler;
    private List<StateButtonAddon> buttonAddons;
    private int xSize;
    private int ySize;
    private boolean clicked;
    private Point inventoryPoint;
    private IAssetType<?> assetType;
    private Direction blockDirection;

    public FacingHandlerScreenAddon(SidedComponentManager manager, IFacingComponent facingHandler, IAssetType<?> assetType, Direction blockDirection) {
        super(manager.getPosX(), manager.getPosY());
        this.handler = facingHandler;
        this.buttonAddons = new ArrayList<>();
        this.xSize = 0;
        this.ySize = 0;
        this.clicked = false;
        this.assetType = assetType;
        this.blockDirection = blockDirection;
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
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        IBackgroundAsset backgroundInfo = provider.getAsset(AssetTypes.BACKGROUND);
        inventoryPoint = backgroundInfo.getInventoryPosition();
        this.xSize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().width;
        this.ySize = provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER).getArea().height;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        AssetUtil.drawAsset(stack, screen, provider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER), guiX + getPosX(), guiY + getPosY());
        int offset = 2;
        GuiComponent.fill(stack, guiX + getPosX() + offset, guiY + getPosY() + offset, guiX + getPosX() + getXSize() - offset, guiY + getPosY() + getYSize() - offset, handler.getColor());
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (isClicked()) {
            //draw the overlay for the slots
            screen.blit(stack, guiX + backgroundInfo.getInventoryPosition().x - 1, guiY + backgroundInfo.getInventoryPosition().y - 1, 16, 213 + 18, 14, 14);
            screen.blit(stack, guiX + backgroundInfo.getInventoryPosition().x - 1, guiY + backgroundInfo.getInventoryPosition().y - 1, 56, 185, 162, 54);
        }
    }

    @Override
    public void drawForegroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        if (isInside(screen, mouseX - guiX, mouseY - guiY)) {
            AssetUtil.drawSelectingOverlay(stack, getPosX() + 1, getPosY() + 1, getPosX() + getXSize() - 1, getPosY() + getYSize() - 1);
        }
        if (isInside(screen, mouseX - guiX, mouseY - guiY) || isClicked()) {
            IAsset asset = provider.getAsset(assetType);
            Rectangle area = handler.getRectangle(asset);
            AssetUtil.drawHorizontalLine(stack, area.x, area.x + area.width, area.y, handler.getColor());
            AssetUtil.drawHorizontalLine(stack, area.x, area.x + area.width, area.y + area.height, handler.getColor());
            AssetUtil.drawVerticalLine(stack, area.x, area.y, area.y + area.height, handler.getColor());
            AssetUtil.drawVerticalLine(stack, area.x + area.width, area.y, area.y + area.height, handler.getColor());
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return Collections.singletonList(LangUtil.get("tooltip.titanium.facing_handler." + handler.getName().toLowerCase()));
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
        if (button == 1) return;
        if (screen instanceof IScreenAddonConsumer && screen instanceof MenuAccess) {
            IScreenAddonConsumer screenAddonConsumer = (IScreenAddonConsumer) screen;
            AbstractContainerMenu container = ((MenuAccess<?>) screen).getMenu();
            Consumer<Boolean> disable = container instanceof IDisableableContainer ?
                ((IDisableableContainer) container)::setDisabled : value -> {
            };
            for (IScreenAddon addon : new ArrayList<>(((IScreenAddonConsumer) screen).getAddons())) {
                if (addon instanceof FacingHandlerScreenAddon && addon != this) {
                    ((FacingHandlerScreenAddon) addon).setClicked(screenAddonConsumer, disable, false);
                    disable.accept(true);
                }
            }
            this.setClicked(screenAddonConsumer, disable, !clicked);
            if (clicked) {
                disable.accept(true);
                for (FacingUtil.Sideness facing : FacingUtil.Sideness.values()) {
                    if (!handler.getFacingModes().containsKey(facing)) continue;
                    Point point = getPointFromFacing(facing, inventoryPoint);
                    List<StateButtonInfo> stateButtonInfo = new ArrayList<>();
                    for (int i = 0; i < handler.getValidFacingModes().length; i++) {
                        stateButtonInfo.add(handler.getValidFacingModes()[i].getInfo(i));
                    }
                    StateButtonAddon addon = new StateButtonAddon(new ButtonComponent(point.x, point.y, 14, 14), stateButtonInfo.toArray(new StateButtonInfo[0])) {
                        @Override
                        public int getState() {
                            IFacingComponent handler = null;
                            if (container instanceof IObjectContainer) {
                                Object containedObject = ((IObjectContainer) container).getObject();
                                if (containedObject instanceof IFacingComponentHarness) {
                                    handler = ((IFacingComponentHarness)containedObject).getHandlerFromName(
                                            FacingHandlerScreenAddon.this.handler.getName());
                                } else {
                                    Titanium.LOGGER.warn("Contained Object is not IFacingComponentHarness. Could not get FacingComponent");
                                }
                            } else {
                                Titanium.LOGGER.warn("Container is not IObjectContainer. Could not get FacingComponent");
                            }
                            return handler != null && handler.getFacingModes().containsKey(facing) ?
                                    Arrays.asList(handler.getValidFacingModes()).indexOf(handler.getFacingModes().get(facing)) : 0;
                        }

                        @Override
                        public void handleClick(Screen gui, int guiX, int guiY, double mouseX, double mouseY, int mouse) {
                            StateButtonInfo info = getStateInfo();
                            if (info != null && gui instanceof MenuAccess<?>) {
                                CompoundTag compound = new CompoundTag();
                                compound.putString("Facing", facing.name());
                                int faceMode = (getState() + (mouse == 0 ? 1 : -1)) % handler.getValidFacingModes().length;
                                if (faceMode < 0) faceMode = handler.getValidFacingModes().length - 1;
                                compound.putInt("Next", faceMode);
                                compound.putString("Name", handler.getName());
                                if (container instanceof ILocatable) {
                                    Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(
                                            ((ILocatable) container).getLocatorInstance(), -1, compound));
                                } else {
                                    Titanium.LOGGER.warn("Failed to Find Locatable Instance for Container");
                                }
                                handler.getFacingModes().put(facing, handler.getValidFacingModes()[faceMode]);
                            }
                        }

                        @Override
                        public List<Component> getTooltipLines() {
                            List<Component> strings = new ArrayList<>();
                            IFacingComponent.FaceMode mode = handler.getValidFacingModes()[getState()];
                            strings.add(new TextComponent(ChatFormatting.GOLD + LangUtil.getString("tooltip.titanium.facing_handler.direction") +
                                ChatFormatting.RESET + LangUtil.getString("tooltip.titanium.facing_handler." + facing.name().toLowerCase()) + ChatFormatting.GRAY + " [" + LangUtil.getString("direction.titanium." + FacingUtil.getFacingFromSide(blockDirection, facing)) + "]"));
                            strings.add(new TextComponent(ChatFormatting.GOLD + LangUtil.getString("tooltip.titanium.facing_handler.action") +
                                mode.getColor() + getStateInfo().getTooltip()[0].getString()));
                            return strings;
                        }
                    };
                    buttonAddons.add(addon);
                    ((IScreenAddonConsumer) screen).getAddons().add(addon);
                }
            }
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(IScreenAddonConsumer information, Consumer<Boolean> disable, boolean clicked) {
        this.clicked = clicked;
        if (!clicked) {
            //noinspection SuspiciousMethodCalls
            information.getAddons().removeIf(iGuiAddon -> buttonAddons.contains(iGuiAddon));
            buttonAddons.clear();
            disable.accept(false);
        }
    }

}

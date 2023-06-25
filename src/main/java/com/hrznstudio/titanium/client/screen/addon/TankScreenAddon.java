/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BucketItem;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TankScreenAddon extends BasicScreenAddon {

    private IFluidTank tank;
    private ITankAsset asset;
    private FluidTankComponent.Type type;

    public TankScreenAddon(int posX, int posY, IFluidTank tank, FluidTankComponent.Type type) {
        super(posX, posY);
        this.tank = tank;
        this.type = type;
    }

    @Override
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        asset = IAssetProvider.getAsset(provider, type.getAssetType());
        Rectangle area = asset.getArea();
        if (!tank.getFluid().isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            double stored = tank.getFluidAmount();
            double capacity = tank.getCapacity();
            int topBottomPadding = asset.getFluidRenderPadding(Direction.UP) + asset.getFluidRenderPadding(Direction.DOWN);
            int offset = (int) ((stored / capacity) * (area.height - topBottomPadding));
            IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
            ResourceLocation flowing = renderProperties.getStillTexture(fluidStack);
            if (flowing != null) {
                AbstractTexture texture = screen.getMinecraft().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS); //getAtlasSprite
                if (texture instanceof TextureAtlas) {
                    TextureAtlasSprite sprite = ((TextureAtlas) texture).getSprite(flowing);
                    if (sprite != null) {
                        //RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
                        Color color = new Color(renderProperties.getTintColor(fluidStack));
                        guiGraphics.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                        RenderSystem.enableBlend();
                        guiGraphics.blit(
                            this.getPosX() + guiX + asset.getFluidRenderPadding(Direction.WEST),
                            this.getPosY() + guiY + asset.getFluidRenderPadding(Direction.UP) + (fluidStack.getFluid().is(Tags.Fluids.GASEOUS) ? 0 : (area.height - topBottomPadding) - offset),
                            0,
                            (int) (area.getWidth() - asset.getFluidRenderPadding(Direction.EAST) - asset.getFluidRenderPadding(Direction.WEST)),
                            offset,
                            sprite);
                        RenderSystem.disableBlend();
                        guiGraphics.setColor(1, 1, 1, 1);
                    }
                }
            }
        }
        guiGraphics.setColor(1, 1, 1, 1);
        ITankAsset asset = IAssetProvider.getAsset(provider, type.getAssetType());
        AssetUtil.drawAsset(guiGraphics, screen, asset, guiX + getPosX(), guiY + getPosY());
    }

    @Override
    public void drawForegroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public List<Component> getTooltipLines() {
        List<Component> strings = new ArrayList<>();
        strings.add(Component.literal(ChatFormatting.GOLD + Component.translatable("tooltip.titanium.tank.fluid").getString()).append(tank.getFluid().isEmpty() ? Component.translatable("tooltip.titanium.tank.empty").withStyle(ChatFormatting.WHITE) : Component.translatable(tank.getFluid().getFluid().getFluidType().getDescriptionId())).withStyle(ChatFormatting.WHITE));
        strings.add(Component.translatable("tooltip.titanium.tank.amount").withStyle(ChatFormatting.GOLD).append(Component.literal(ChatFormatting.WHITE + new DecimalFormat().format(tank.getFluidAmount()) + ChatFormatting.GOLD + "/" + ChatFormatting.WHITE + new DecimalFormat().format(tank.getCapacity()) + ChatFormatting.DARK_AQUA + "mb")));
        if (!Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && Minecraft.getInstance().player.containerMenu.getCarried().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
            Minecraft.getInstance().player.containerMenu.getCarried().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
                boolean isBucket = Minecraft.getInstance().player.containerMenu.getCarried().getItem() instanceof BucketItem;
                int amount = isBucket ? FluidType.BUCKET_VOLUME : Integer.MAX_VALUE;
                boolean canFillFromItem = false;
                boolean canDrainFromItem = false;
                if (isBucket) {
                    canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidType.BUCKET_VOLUME;
                    canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidType.BUCKET_VOLUME;
                } else {
                    canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                    canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                }
                if (canFillFromItem)
                    strings.add(Component.translatable("tooltip.titanium.tank.can_fill_from_item").withStyle(ChatFormatting.BLUE));
                if (canDrainFromItem)
                    strings.add(Component.translatable("tooltip.titanium.tank.can_drain_from_item").withStyle(ChatFormatting.GOLD));
                if (canFillFromItem)
                    strings.add(Component.translatable("tooltip.titanium.tank.action_fill").withStyle(ChatFormatting.DARK_GRAY));
                if (canDrainFromItem)
                    strings.add(Component.translatable("tooltip.titanium.tank.action_drain").withStyle(ChatFormatting.DARK_GRAY));
                if (!canDrainFromItem && !canFillFromItem) {
                    strings.add(Component.translatable("tooltip.titanium.tank.no_action").withStyle(ChatFormatting.RED));
                }
            });
        } else {
            strings.add(Component.translatable("tooltip.titanium.tank.no_tank").withStyle(ChatFormatting.DARK_GRAY));
        }
        return strings;
    }

    @Override
    public int getXSize() {
        return asset != null ? asset.getArea().width : 0;
    }

    @Override
    public int getYSize() {
        return asset != null ? asset.getArea().height : 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && Minecraft.getInstance().player.containerMenu.getCarried().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
            Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof AbstractContainerScreen && ((AbstractContainerScreen) screen).getMenu() instanceof ILocatable) {
                if (!isMouseOver(mouseX - ((AbstractContainerScreen<?>) screen).getGuiLeft(), mouseY - ((AbstractContainerScreen<?>) screen).getGuiTop()))
                    return false;
                Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS, 1f, 1f, RandomSource.create(), Minecraft.getInstance().player.blockPosition())); //getPosition
                ILocatable locatable = (ILocatable) ((AbstractContainerScreen) screen).getMenu();
                CompoundTag compoundNBT = new CompoundTag();
                if (tank instanceof FluidTankComponent) {
                    compoundNBT.putString("Name", ((FluidTankComponent<?>) tank).getName());
                } else {
                    compoundNBT.putBoolean("Invalid", true);
                }
                Minecraft.getInstance().player.containerMenu.getCarried().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
                    boolean isBucket = Minecraft.getInstance().player.containerMenu.getCarried().getItem() instanceof BucketItem;
                    int amount = isBucket ? FluidType.BUCKET_VOLUME : Integer.MAX_VALUE;
                    boolean canFillFromItem = false;
                    boolean canDrainFromItem = false;
                    if (isBucket) {
                        canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidType.BUCKET_VOLUME;
                        canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidType.BUCKET_VOLUME;
                    } else {
                        canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                        canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                    }
                    if (canFillFromItem && button == 0) compoundNBT.putBoolean("Fill", true);
                    if (canDrainFromItem && button == 1) compoundNBT.putBoolean("Fill", false);
                });
                Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), -3, compoundNBT));
                return true;
            }
        }
        return false;
    }
}

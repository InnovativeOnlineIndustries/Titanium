/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.BucketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TankScreenAddon extends BasicScreenAddon implements IClickable {

    private IFluidTank tank;
    private ITankAsset asset;
    private FluidTankComponent.Type type;

    public TankScreenAddon(int posX, int posY, IFluidTank tank, FluidTankComponent.Type type) {
        super(posX, posY);
        this.tank = tank;
        this.type = type;
    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        asset = IAssetProvider.getAsset(provider, type.getAssetType());
        Rectangle area = asset.getArea();
        if (!tank.getFluid().isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            double stored = tank.getFluidAmount();
            double capacity = tank.getCapacity();
            int topBottomPadding = asset.getFluidRenderPadding(Direction.UP) + asset.getFluidRenderPadding(Direction.DOWN);
            int offset = (int) ((stored / capacity) * (area.height - topBottomPadding));
            ResourceLocation flowing = fluidStack.getFluid().getAttributes().getStillTexture(fluidStack);
            if (flowing != null) {
                Texture texture = screen.getMinecraft().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE); //getAtlasSprite
                if (texture instanceof AtlasTexture) {
                    TextureAtlasSprite sprite = ((AtlasTexture) texture).getSprite(flowing);
                    if (sprite != null) {
                        screen.getMinecraft().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                        Color color = new Color(fluidStack.getFluid().getAttributes().getColor(fluidStack));
                        RenderSystem.color4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                        RenderSystem.enableBlend();
                        Screen.blit(stack, this.getPosX() + guiX + asset.getFluidRenderPadding(Direction.WEST),
                            this.getPosY() + guiY + asset.getFluidRenderPadding(Direction.UP) + (fluidStack.getFluid().getAttributes().isGaseous() ? 0 : (area.height - topBottomPadding) - offset),
                            0,
                            (int) (area.getWidth() - asset.getFluidRenderPadding(Direction.EAST) - asset.getFluidRenderPadding(Direction.WEST)),
                            offset,
                            sprite);
                        RenderSystem.disableBlend();
                        RenderSystem.color4f(1, 1, 1, 1);
                    }
                }
            }
        }
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.enableAlphaTest();
        ITankAsset asset = IAssetProvider.getAsset(provider, type.getAssetType());
        AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX(), guiY + getPosY());
    }

    @Override
    public void drawForegroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {}

    @Override
    public List<ITextComponent> getTooltipLines() {
        List<ITextComponent> strings = new ArrayList<>();
        strings.add(new StringTextComponent(TextFormatting.GOLD + new TranslationTextComponent("tooltip.titanium.tank.fluid").getString()).append(tank.getFluid().isEmpty() ? new TranslationTextComponent("tooltip.titanium.tank.empty").mergeStyle(TextFormatting.WHITE) :  new TranslationTextComponent(tank.getFluid().getFluid().getAttributes().getTranslationKey(tank.getFluid()))).mergeStyle(TextFormatting.WHITE));
        strings.add(new TranslationTextComponent("tooltip.titanium.tank.amount").mergeStyle(TextFormatting.GOLD).append(new StringTextComponent(TextFormatting.WHITE + new DecimalFormat().format(tank.getFluidAmount()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(tank.getCapacity()) + TextFormatting.DARK_AQUA + "mb")));
        if (!Minecraft.getInstance().player.inventory.getItemStack().isEmpty() && Minecraft.getInstance().player.inventory.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()){
            Minecraft.getInstance().player.inventory.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(iFluidHandlerItem -> {
                boolean isBucket = Minecraft.getInstance().player.inventory.getItemStack().getItem() instanceof BucketItem;
                int amount = isBucket ? FluidAttributes.BUCKET_VOLUME : Integer.MAX_VALUE;
                boolean canFillFromItem = false;
                boolean canDrainFromItem = false;
                if (isBucket) {
                    canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME;
                    canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME;
                } else {
                    canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                    canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                }
                if (canFillFromItem)
                    strings.add(new TranslationTextComponent("tooltip.titanium.tank.can_fill_from_item").mergeStyle(TextFormatting.BLUE));
                if (canDrainFromItem)
                    strings.add(new TranslationTextComponent("tooltip.titanium.tank.can_drain_from_item").mergeStyle(TextFormatting.GOLD));
                if (canFillFromItem)
                    strings.add(new TranslationTextComponent("tooltip.titanium.tank.action_fill").mergeStyle(TextFormatting.DARK_GRAY));
                if (canDrainFromItem)
                    strings.add(new TranslationTextComponent("tooltip.titanium.tank.action_drain").mergeStyle(TextFormatting.DARK_GRAY));
                if (!canDrainFromItem && !canFillFromItem) {
                    strings.add(new TranslationTextComponent("tooltip.titanium.tank.no_action").mergeStyle(TextFormatting.RED));
                }
            });
        } else {
            strings.add(new TranslationTextComponent("tooltip.titanium.tank.no_tank").mergeStyle(TextFormatting.DARK_GRAY));
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
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        if (!Minecraft.getInstance().player.inventory.getItemStack().isEmpty() && Minecraft.getInstance().player.inventory.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()){
            Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f, Minecraft.getInstance().player.getPosition())); //getPosition
            if (screen instanceof ContainerScreen && ((ContainerScreen) screen).getContainer() instanceof ILocatable) {
                ILocatable locatable = (ILocatable) ((ContainerScreen) screen).getContainer();
                CompoundNBT compoundNBT = new CompoundNBT();
                if (tank instanceof FluidTankComponent){
                    compoundNBT.putString("Name",((FluidTankComponent<?>) tank).getName());
                } else {
                    compoundNBT.putBoolean("Invalid", true);
                }
                Minecraft.getInstance().player.inventory.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(iFluidHandlerItem -> {
                    boolean isBucket = Minecraft.getInstance().player.inventory.getItemStack().getItem() instanceof BucketItem;
                    int amount = isBucket ? FluidAttributes.BUCKET_VOLUME : Integer.MAX_VALUE;
                    boolean canFillFromItem = false;
                    boolean canDrainFromItem = false;
                    if (isBucket) {
                        canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME;
                        canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME;
                    } else {
                        canFillFromItem = tank.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                        canDrainFromItem = iFluidHandlerItem.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0;
                    }
                    if (canFillFromItem && button == 0) compoundNBT.putBoolean("Fill", true);
                    if (canDrainFromItem && button == 1) compoundNBT.putBoolean("Fill", false);
                });
                Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), -3, compoundNBT));
            }
        }
    }
}

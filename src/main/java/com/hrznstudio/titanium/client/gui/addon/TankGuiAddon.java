/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class TankGuiAddon extends BasicGuiAddon {

    private PosFluidTank tank;

    public TankGuiAddon(PosFluidTank tank) {
        super(tank.getPosX(), tank.getPosY());
        this.tank = tank;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        ITankAsset asset = IAssetProvider.getAsset(provider, AssetTypes.TANK);
        Rectangle area = asset.getArea();
        if (tank.getFluid() != null) {
            FluidStack stack = tank.getFluid();
            double filledAmount = tank.getFluidAmount() / (double) tank.getCapacity();
            ResourceLocation flowing = stack.getFluid().getFlowing();
            if (flowing != null) {
                TextureAtlasSprite sprite = screen.mc.getTextureMap().getAtlasSprite(flowing.toString());
                if (sprite == null) sprite = MissingTextureSprite.getSprite();
                screen.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.enableBlend();
                int topBottomPadding = asset.getFluidRenderPadding(EnumFacing.UP) + asset.getFluidRenderPadding(EnumFacing.DOWN);
                screen.drawTexturedModalRect(this.getPosX() + guiX + asset.getFluidRenderPadding(EnumFacing.WEST),
                        (int) (this.getPosY() + guiY + asset.getFluidRenderPadding(EnumFacing.UP) + (stack.getFluid().isGaseous() ? area.height - topBottomPadding : (area.height - topBottomPadding) - (area.height - topBottomPadding) * filledAmount)),
                        sprite,
                        area.width - asset.getFluidRenderPadding(EnumFacing.WEST) - asset.getFluidRenderPadding(EnumFacing.WEST),
                        (int) ((area.height - topBottomPadding) * filledAmount));
                GlStateManager.disableBlend();
            }
        }
        Point offset = asset.getOffset();
        screen.mc.getTextureManager().bindTexture(asset.getResourceLocation());
        screen.drawTexturedModalRect(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiScreen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipLines() { ///TODO localize
        return Arrays.asList("Fluid: " + (tank.getFluid() == null ? "Empty" : tank.getFluid().getFluid().getLocalizedName(tank.getFluid())), "Amount: " + new DecimalFormat().format(tank.getFluidAmount()) + "/" + new DecimalFormat().format(tank.getCapacity()) + "mb");
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
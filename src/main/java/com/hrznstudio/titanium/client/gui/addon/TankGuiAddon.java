/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
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
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        ITankAsset asset = IAssetProvider.getAsset(provider, AssetTypes.TANK);
        Rectangle area = asset.getArea();
        if (tank.getFluid() != null) {
            FluidStack stack = tank.getFluid();
            double filledAmount = tank.getFluidAmount() / (double) tank.getCapacity();
            ResourceLocation flowing = stack.getFluid().getFlowing();
            if (flowing != null) {
                TextureAtlasSprite sprite = screen.getMinecraft().getTextureMap().getAtlasSprite(flowing.toString());
                if (sprite == null) sprite = MissingTextureSprite.func_217790_a();
                screen.getMinecraft().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.enableBlend();
                int topBottomPadding = asset.getFluidRenderPadding(Direction.UP) + asset.getFluidRenderPadding(Direction.DOWN);
                screen.blit(this.getPosX() + guiX + asset.getFluidRenderPadding(Direction.WEST),
                        (int) (this.getPosY() + guiY + asset.getFluidRenderPadding(Direction.UP) + (stack.getFluid().isGaseous() ? area.height - topBottomPadding : (area.height - topBottomPadding) - (area.height - topBottomPadding) * filledAmount)),
                        area.width - asset.getFluidRenderPadding(Direction.WEST) - asset.getFluidRenderPadding(Direction.WEST),
                        (int) ((area.height - topBottomPadding) * filledAmount), sprite.getHeight(),
                        sprite);
                GlStateManager.disableBlend();
            }
        }
        Point offset = asset.getOffset();
        screen.getMinecraft().getTextureManager().bindTexture(asset.getResourceLocation());
        screen.blit(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

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
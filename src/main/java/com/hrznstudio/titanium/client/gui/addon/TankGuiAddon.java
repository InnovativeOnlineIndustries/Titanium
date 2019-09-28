/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class TankGuiAddon extends BasicGuiAddon {

    private PosFluidTank tank;
    private ITankAsset asset;

    public TankGuiAddon(PosFluidTank tank) {
        super(tank.getPosX(), tank.getPosY());
        this.tank = tank;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        asset = (ITankAsset) IAssetProvider.getAsset(provider, tank.getTankType().getAssetType());
        Rectangle area = asset.getArea();
        if (!tank.getFluid().isEmpty()) {
            FluidStack stack = tank.getFluid();
            int stored = tank.getFluidAmount();
            int capacity = tank.getCapacity();
            int topBottomPadding = asset.getFluidRenderPadding(Direction.UP) + asset.getFluidRenderPadding(Direction.DOWN);
            int offset = (stored * (area.height - topBottomPadding) / capacity);
            ResourceLocation flowing = stack.getFluid().getAttributes().getStill(stack);
            if (flowing != null) {
                TextureAtlasSprite sprite = screen.getMinecraft().getTextureMap().getAtlasSprite(flowing.toString());
                if (sprite == null) sprite = MissingTextureSprite.func_217790_a();
                screen.getMinecraft().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                Color color = new Color(stack.getFluid().getAttributes().getColor());
                GlStateManager.color4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                GlStateManager.enableBlend();
                screen.blit(this.getPosX() + guiX + asset.getFluidRenderPadding(Direction.WEST),
                        (int) (this.getPosY() + guiY + asset.getFluidRenderPadding(Direction.UP) + (stack.getFluid().getAttributes().isGaseous() ? area.height - topBottomPadding : (area.height - topBottomPadding) - offset)),
                        0,
                        (int) (area.getWidth() - asset.getFluidRenderPadding(Direction.EAST) - asset.getFluidRenderPadding(Direction.WEST)),
                        offset,
                        sprite);
                GlStateManager.disableBlend();
                GlStateManager.color4f(1, 1, 1, 1);
            }
        }
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.enableAlphaTest();
        ITankAsset asset = (ITankAsset) IAssetProvider.getAsset(provider, tank.getTankType().getAssetType());
        AssetUtil.drawAsset(screen, asset, guiX + getPosX(), guiY + getPosY());
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipLines() { ///TODO localize
        return Arrays.asList(TextFormatting.GOLD + "Fluid: " + TextFormatting.WHITE + (tank.getFluid().isEmpty() ? "Empty" : new TranslationTextComponent(tank.getFluid().getFluid().getAttributes().getTranslationKey(tank.getFluid())).getFormattedText()), TextFormatting.GOLD + "Amount: " + TextFormatting.WHITE + new DecimalFormat().format(tank.getFluidAmount()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(tank.getCapacity()) + TextFormatting.DARK_AQUA + "mb");
    }

    @Override
    public int getXSize() {
        return asset != null ? asset.getArea().width : 0;
    }

    @Override
    public int getYSize() {
        return asset != null ? asset.getArea().height : 0;
    }

}
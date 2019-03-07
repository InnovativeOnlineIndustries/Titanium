/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium._test.BlockTest;
import com.hrznstudio.titanium._test.BlockTwentyFourTest;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.addon.BasicButtonAddon;
import com.hrznstudio.titanium.container.ContainerTileBase;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.TileUtil;
import com.hrznstudio.titanium.util.TitaniumMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod(Titanium.MODID)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";

    public static AdvancedTitaniumTab RESOURCES_TAB;

    public Titanium() {
        addBlock(BlockTest.TEST = new BlockTest());
        addBlock(BlockTwentyFourTest.TEST = new BlockTwentyFourTest());

        NetworkHandler.registerMessage(BasicButtonAddon.ButtonClickNetworkMessage.class);
    }

    public static void openGui(TileActive tile, EntityPlayerMP player) {
        NetworkHooks.openGui(player, tile, buf -> {
            buf.writeInt(tile.getPos().getX());
            buf.writeInt(tile.getPos().getY());
            buf.writeInt(tile.getPos().getZ());
        });
    }

    @EventReceiver
    public void clientSetup(FMLClientSetupEvent event) {
        TitaniumClient.registerModelLoader();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> data -> {
            int x = data.getAdditionalData().readInt();
            int y = data.getAdditionalData().readInt();
            int z = data.getAdditionalData().readInt();
            return TileUtil.getTileEntity(
                    Minecraft.getInstance().world,
                    new BlockPos(x, y, z),
                    TileActive.class
            ).map(tile -> new GuiContainerTile<>(
                    new ContainerTileBase<>(
                            tile,
                            Minecraft.getInstance().player.inventory
                    )
            )).orElse(null);
        });
    }

    @SubscribeEvent
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = event.getTarget().getBlockPos();
        RayTraceResult hit = event.getTarget();
        if (hit.type == RayTraceResult.Type.BLOCK && hit instanceof DistanceRayTraceResult) {
            event.setCanceled(true);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.lineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            EntityPlayer player = event.getPlayer();
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
            WorldRenderer.drawShape(((DistanceRayTraceResult) hit).getHitBox().withOffset(pos.getX(),pos.getY(),pos.getZ()), -x, -y, -z,0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }
}
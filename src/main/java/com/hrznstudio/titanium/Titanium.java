/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium._test.BlockTest;
import com.hrznstudio.titanium._test.BlockTwentyFourTest;
import com.hrznstudio.titanium._test.TitaniumConfig;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.client.gui.addon.BasicButtonAddon;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.util.TileUtil;
import com.hrznstudio.titanium.util.TitaniumMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod(Titanium.MODID)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";

    public Titanium() {
        registerEntries();
        NetworkHandler.registerMessage(BasicButtonAddon.ButtonClickNetworkMessage.class);
        addConfig(new AnnotationConfigManager.Type(ModConfig.Type.COMMON, TitaniumConfig.class));
        EventManager.subscribe(EntityItemPickupEvent.class).filter(entityItemPickupEvent -> entityItemPickupEvent.getItem().getItem().getItem().equals(Items.STICK)).process(entityItemPickupEvent -> entityItemPickupEvent.getItem().lifespan = 0).cancel();
    }

    public static void openGui(TileActive tile, EntityPlayerMP player) {
        NetworkHooks.openGui(player, tile, buf -> {
            buf.writeInt(tile.getPos().getX());
            buf.writeInt(tile.getPos().getY());
            buf.writeInt(tile.getPos().getZ());
        });
    }

    private void registerEntries() {
        addEntry(Block.class, BlockTest.TEST = new BlockTest());
        addEntry(Block.class, BlockTwentyFourTest.TEST = new BlockTwentyFourTest());
    }

    @EventReceiver
    @OnlyIn(Dist.CLIENT)
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
            ).map(tile -> tile.createGui(tile.createContainer(Minecraft.getInstance().player.inventory, Minecraft.getInstance().player))
            ).orElse(null);
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
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
            WorldRenderer.drawShape(((DistanceRayTraceResult) hit).getHitBox().withOffset(pos.getX(), pos.getY(), pos.getZ()), -x, -y, -z, 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }
}
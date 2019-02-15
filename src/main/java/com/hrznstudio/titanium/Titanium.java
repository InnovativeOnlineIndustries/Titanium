/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium;

import com.hrznstudio.titanium._test.BlockTest;
import com.hrznstudio.titanium._test.BlockTwentyFourTest;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.client.gui.GuiHandler;
import com.hrznstudio.titanium.network.OpenGUI;
import com.hrznstudio.titanium.network.Packet;
import com.hrznstudio.titanium.pulsar.control.PulseManager;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.SidedHandler;
import com.hrznstudio.titanium.util.TitaniumMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Mod(Titanium.MODID)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";

    public static AdvancedTitaniumTab RESOURCES_TAB;

    public static PulseManager COMPAT_MANAGER = null;
    private static boolean vanilla;

    private static SimpleChannel NETWORK;

    public static IGuiHandler guiHandler = new GuiHandler();

    public static void openGui(TileBase tile, EntityPlayerMP player) {
        player.getNextWindowId();
        player.openContainer = (Container) guiHandler.getServerGuiElement(0, player, player.world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
        if(player.openContainer!=null) {
            player.openContainer.windowId = player.currentWindowId;
            player.openContainer.addListener(player);
            NETWORK.sendTo(new OpenGUI(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public Titanium() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventReceiver
    public void setup(FMLCommonSetupEvent setup) {
        COMPAT_MANAGER = new PulseManager("titanium/compat");
        addBlock(BlockTest.TEST = new BlockTest());
        addBlock(BlockTwentyFourTest.TEST = new BlockTwentyFourTest());
        SidedHandler.runOn(Dist.CLIENT, () -> TitaniumClient::registerModelLoader);
    }

    public static <P extends Packet> BiConsumer<P, PacketBuffer> encode() {
        return Packet::encode;
    }

    public static <P extends Packet> Function<PacketBuffer, P> decode(Class<P> packetClass) {
        return buffer -> {
            try {
                Constructor constructor = packetClass.getDeclaredConstructor(PacketBuffer.class);
                return packetClass.cast(constructor.newInstance(buffer));
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                try {
                    Constructor constructor = packetClass.getDeclaredConstructor();
                    P p = packetClass.cast(constructor.newInstance());
                    p.decode(buffer);
                    return p;
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e1) {
                    throw new RuntimeException(e1);
                }
            }
        };
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
            WorldRenderer.drawSelectionBoundingBox(((DistanceRayTraceResult) hit).getHitBox().offset(-x, -y, -z).offset(pos).grow(0.002), 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }
}
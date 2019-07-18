/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium._impl.creative.BlockCreativeFEGenerator;
import com.hrznstudio.titanium._impl.test.BlockTest;
import com.hrznstudio.titanium._impl.test.BlockTwentyFourTest;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.addon.BasicButtonAddon;
import com.hrznstudio.titanium.container.ContainerTileBase;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.util.SidedHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod(Titanium.MODID)
public class Titanium extends ModuleController {
    public static final String MODID = "titanium";

    public Titanium() {
        NetworkHandler.registerMessage(BasicButtonAddon.ButtonClickNetworkMessage.class);

        SidedHandler.runOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(this::clientSetup).subscribe());
    }

    public static void openGui(TileActive tile, ServerPlayerEntity player) {
        NetworkHooks.openGui(player, tile, tile.getPos());
    }

    @Override
    protected void initModules() {
        addModule(Module.builder("core").force().feature(Feature.builder("core").force().content(ContainerType.class, (ContainerType) IForgeContainerType.create(ContainerTileBase::new).setRegistryName(new ResourceLocation(Titanium.MODID, "tile_container")))));
        addModule(Module.builder("test_module")
                .disableByDefault()
                .description("Test module for titanium features")
                .feature(Feature.builder("blocks")
                        .description("Adds test titanium blocks")
                        .content(Block.class, BlockTest.TEST = new BlockTest())
                        .content(Block.class, BlockTwentyFourTest.TEST = new BlockTwentyFourTest())
                )
                .feature(Feature.builder("events")
                        .description("Adds test titanium events")
                        .event(EventManager.forge(EntityItemPickupEvent.class).filter(ev -> ev.getItem().getItem().getItem() == Items.STICK).process(ev -> ev.getItem().lifespan = 0).cancel())
                )
        );
        addModule(Module.builder("creative")
                .disableByDefault()
                .description("Creative features")
                .feature(Feature.builder("blocks")
                        .description("Adds creative machine features")
                        .content(Block.class, BlockCreativeFEGenerator.INSTANCE)));
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        EventManager.forge(DrawBlockHighlightEvent.class).process(this::drawBlockHighlight).subscribe();
        TitaniumClient.registerModelLoader();
        ScreenManager.registerFactory(ContainerTileBase.TYPE, GuiContainerTile::new);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = new BlockPos(event.getTarget().getHitVec().x, event.getTarget().getHitVec().y, event.getTarget().getHitVec().z);
        RayTraceResult hit = event.getTarget();
        if (hit.getType() == RayTraceResult.Type.BLOCK && hit instanceof DistanceRayTraceResult) {
            event.setCanceled(true);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.lineWidth(2.0F);
            GlStateManager.disableTexture();
            GlStateManager.depthMask(false);
            PlayerEntity player = Minecraft.getInstance().player;
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
            WorldRenderer.drawShape(((DistanceRayTraceResult) hit).getHitBox().withOffset(pos.getX(), pos.getY(), pos.getZ()), -x, -y, -z, 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture();
            GlStateManager.disableBlend();
        }
    }
}
/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium._impl.creative.BlockCreativeFEGenerator;
import com.hrznstudio.titanium._impl.test.BlockAssetTest;
import com.hrznstudio.titanium._impl.test.BlockMachine;
import com.hrznstudio.titanium._impl.test.BlockTest;
import com.hrznstudio.titanium._impl.test.BlockTwentyFourTest;
import com.hrznstudio.titanium._impl.test.recipe.TestSerializableRecipe;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.client.gui.container.GuiContainerTileBase;
import com.hrznstudio.titanium.command.RewardCommand;
import com.hrznstudio.titanium.command.RewardGrantCommand;
import com.hrznstudio.titanium.container.impl.ContainerTileBase;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.recipe.generator.titanium.DefaultLootTableProvider;
import com.hrznstudio.titanium.recipe.generator.titanium.JsonRecipeSerializerProvider;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.RewardSyncMessage;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.hrznstudio.titanium.util.SidedHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


@Mod(Titanium.MODID)
public class Titanium extends ModuleController {

    public static final String MODID = "titanium";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Titanium() {
        NetworkHandler.registerMessage(ButtonClickNetworkMessage.class);
        NetworkHandler.registerMessage(RewardSyncMessage.class);

        SidedHandler.runOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(this::clientSetup).subscribe());
        EventManager.mod(FMLCommonSetupEvent.class).process(this::commonSetup).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class).process(this::onPlayerLoggedIn).subscribe();
        EventManager.forge(FMLServerStartingEvent.class).process(this::onServerStart).subscribe();
    }

    public static void openGui(TileActive tile, ServerPlayerEntity player) {
        NetworkHooks.openGui(player, tile, tile.getPos());
    }

    @Override
    protected void initModules() {
        addModule(Module.builder("core").force()
                .feature(Feature.builder("core").force()
                        .content(ContainerType.class, (ContainerType) IForgeContainerType.create(ContainerTileBase::new).setRegistryName(new ResourceLocation(Titanium.MODID, "tile_container")))));
        addModule(Module.builder("test_module")
                .disableByDefault()
                .description("Test module for titanium features")
                .feature(Feature.builder("blocks")
                        .description("Adds test titanium blocks")
                        .content(Block.class, BlockTest.TEST = new BlockTest())
                        .content(Block.class, BlockTwentyFourTest.TEST = new BlockTwentyFourTest())
                        .content(Block.class, BlockAssetTest.TEST = new BlockAssetTest())
                        .content(Block.class, BlockMachine.TEST = new BlockMachine())
                )
                .feature(Feature.builder("events")
                        .description("Adds test titanium events")
                        .event(EventManager.forge(EntityItemPickupEvent.class).filter(ev -> ev.getItem().getItem().getItem() == Items.STICK).process(ev -> ev.getItem().lifespan = 0).cancel())
                )
                .feature(Feature.builder("recipe")
                        .description("Testing of recipe stuff")
                        .content(IRecipeSerializer.class, (IRecipeSerializer) TestSerializableRecipe.SERIALIZER)
                        .event(EventManager.mod(FMLCommonSetupEvent.class).process(event -> Registry.register(Registry.RECIPE_TYPE, TestSerializableRecipe.SERIALIZER.getRegistryName(), TestSerializableRecipe.SERIALIZER.getRecipeType())))
                        .event(EventManager.forge(PlayerInteractEvent.LeftClickBlock.class)
                                .filter(leftClickBlock -> !leftClickBlock.getWorld().isRemote && leftClickBlock.getPlayer() != null)
                                .process(leftClickBlock -> {
                                    Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, leftClickBlock.getWorld().getRecipeManager(), "field_199522_d");
                                    recipes.get(TestSerializableRecipe.SERIALIZER.getRecipeType()).values().stream()
                                            .map(iRecipe -> (TestSerializableRecipe) iRecipe)
                                            .filter(testSerializableRecipe -> testSerializableRecipe.isValid(leftClickBlock.getPlayer().getHeldItem(leftClickBlock.getHand()), leftClickBlock.getWorld().getBlockState(leftClickBlock.getPos()).getBlock()))
                                            .findFirst().ifPresent(testSerializableRecipe -> {
                                        leftClickBlock.getPlayer().getHeldItem(leftClickBlock.getHand()).shrink(1);
                                        ItemHandlerHelper.giveItemToPlayer(leftClickBlock.getPlayer(), testSerializableRecipe.getRecipeOutput().copy());
                                        leftClickBlock.setCanceled(true);
                                    });
                                }))
                )
        );
        addModule(Module.builder("creative")
                .disableByDefault()
                .description("Creative features")
                .feature(Feature.builder("blocks")
                        .description("Adds creative machine features")
                        .content(Block.class, BlockCreativeFEGenerator.INSTANCE)));
    }

    @Override
    public void addDataProvider(GatherDataEvent event) {
        event.getGenerator().addProvider(new DefaultLootTableProvider(event.getGenerator(), MODID));
        event.getGenerator().addProvider(new JsonRecipeSerializerProvider(event.getGenerator(), MODID));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.DEDICATED_SERVER)));
        LocatorTypes.register();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        EventManager.forge(DrawBlockHighlightEvent.class).process(TitaniumClient::blockOverlayEvent).subscribe();
        TitaniumClient.registerModelLoader();
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.CLIENT)));
        ScreenManager.registerFactory(ContainerTileBase.TYPE, GuiContainerTileBase::new);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().getServer().execute(() -> {
            RewardWorldStorage storage = RewardWorldStorage.get(event.getPlayer().getServer().getWorld(DimensionType.OVERWORLD));
            if (!storage.getConfiguredPlayers().contains(event.getPlayer().getUniqueID())) {
                for (ResourceLocation collectRewardsResourceLocation : RewardManager.get().collectRewardsResourceLocations(event.getPlayer().getUniqueID())) {
                    Reward reward = RewardManager.get().getReward(collectRewardsResourceLocation);
                    storage.add(event.getPlayer().getUniqueID(), reward.getResourceLocation(), reward.getOptions()[0]);
                }
                storage.getConfiguredPlayers().add(event.getPlayer().getUniqueID());
                storage.markDirty();
            }
            CompoundNBT nbt = storage.serializeSimple();
            event.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> NetworkHandler.NETWORK.sendTo(new RewardSyncMessage(nbt), serverPlayerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT));
        });
    }

    private void onServerStart(FMLServerStartingEvent event) {
        RewardCommand.register(event.getCommandDispatcher());
        RewardGrantCommand.register(event.getCommandDispatcher());
    }
}
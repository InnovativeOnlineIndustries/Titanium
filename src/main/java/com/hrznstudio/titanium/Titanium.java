/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium._impl.creative.CreativeFEGeneratorBlock;
import com.hrznstudio.titanium._impl.test.AssetTestBlock;
import com.hrznstudio.titanium._impl.test.MachineTestBlock;
import com.hrznstudio.titanium._impl.test.TestBlock;
import com.hrznstudio.titanium._impl.test.TwentyFourTestBlock;
import com.hrznstudio.titanium._impl.test.recipe.TestSerializableRecipe;
import com.hrznstudio.titanium.attachment.StoredEnergyAttachment;
import com.hrznstudio.titanium.block_network.NetworkManager;
import com.hrznstudio.titanium.command.RewardCommand;
import com.hrznstudio.titanium.command.RewardGrantCommand;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.item.AugmentWrapper;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.network.messages.TileFieldNetworkMessage;
import com.hrznstudio.titanium.recipe.condition.ContentExistsCondition;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.RewardSyncMessage;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.hrznstudio.titanium.util.SidedHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


@Mod(Titanium.MODID)
public class Titanium extends ModuleController {

    public static final String MODID = "titanium";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static NetworkHandler NETWORK = new NetworkHandler(MODID);

    public Titanium(ModContainer container) {
        super(container);
        NETWORK.registerMessage("button_click", ButtonClickNetworkMessage.class);
        NETWORK.registerMessage("reward_sync", RewardSyncMessage.class);
        NETWORK.registerMessage("tile_field", TileFieldNetworkMessage.class);
        SidedHandler.runOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(this::clientSetup).subscribe());
        EventManager.mod(FMLCommonSetupEvent.class).process(this::commonSetup).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class).process(this::onPlayerLoggedIn).subscribe();
        EventManager.forge(ServerStartingEvent.class).process(this::onServerStart).subscribe();

        EventManager.mod(RegisterEvent.class).process(event -> event.register(NeoForgeRegistries.Keys.CONDITION_CODECS, reg ->
            reg.register(ContentExistsCondition.NAME, ContentExistsCondition.CODEC)));
    }

    @Override
    public void onPreInit() {
        super.onPreInit();
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    protected void initModules() {
        BasicAddonContainer.TYPE = getRegistries().registerGeneric(Registries.MENU, "addon_container", () -> (MenuType) IMenuTypeExtension.create(BasicAddonContainer::create));
        StoredEnergyAttachment.TYPE = getRegistries()
            .registerTyped(Registries.DATA_COMPONENT_TYPE, "stored_energy", () -> DataComponentType.<StoredEnergyAttachment>builder()
                .persistent(StoredEnergyAttachment.CODEC)
                .build());
        AugmentWrapper.ATTACHMENT = getRegistries()
            .registerTyped(Registries.DATA_COMPONENT_TYPE, "augments", () -> DataComponentType.<Map<String, Float>>builder()
                .persistent(Codec.unboundedMap(Codec.STRING, Codec.FLOAT))
                .build());
        if (!FMLLoader.getCurrent().isProduction()) { //ENABLE IN DEV
            TestSerializableRecipe.SERIALIZER = getRegistries().registerGeneric(Registries.RECIPE_SERIALIZER, "test_serializer", () -> GenericSerializer.create(TestSerializableRecipe.class, TestSerializableRecipe.RECIPE_TYPE::value, TestSerializableRecipe.CODEC));
            TestSerializableRecipe.RECIPE_TYPE = getRegistries().registerGeneric(Registries.RECIPE_TYPE, "test_recipe_type", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(MODID, "test_recipe_type")));
            TestBlock.TEST = getRegistries().registerBlockWithTile("block_test", () -> (TestBlock) new TestBlock(), null);
            TwentyFourTestBlock.TEST = getRegistries().registerBlockWithTile("block_twenty_four_test", () -> (TwentyFourTestBlock) new TwentyFourTestBlock(), null);
            AssetTestBlock.TEST = getRegistries().registerBlockWithTile("block_asset_test", () -> (AssetTestBlock) new AssetTestBlock(), null);
            MachineTestBlock.TEST = getRegistries().registerBlockWithTile("machine_test", () -> (MachineTestBlock) new MachineTestBlock(), null);
            CreativeFEGeneratorBlock.INSTANCE = getRegistries().registerBlockWithTile("creative_generator", () -> new CreativeFEGeneratorBlock(), null);
        }
    }

    @Override
    public void addDataProvider(GatherDataEvent event) {
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (!FMLLoader.getCurrent().isProduction()) {
            //event.enqueueWork(TestSerializableRecipe::initializeExamples);
        }
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.DEDICATED_SERVER)));
        LocatorTypes.register();
        EventManager.forge(LevelTickEvent.Post.class)
            .filter(worldTickEvent -> !worldTickEvent.getLevel().isClientSide())
            .process(worldTickEvent -> {
                NetworkManager.get(worldTickEvent.getLevel()).getNetworks().forEach(network -> network.update(worldTickEvent.getLevel()));
            }).subscribe();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        EventManager.forge(ExtractBlockOutlineRenderStateEvent.class).process(TitaniumClient::blockOverlayEvent).subscribe();
        TitaniumClient.registerModelLoader();
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.CLIENT)));
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.level().getServer().execute(() -> {
            RewardWorldStorage storage = RewardWorldStorage.get(player.level().getServer().getLevel(Level.OVERWORLD));
            if (!storage.getConfiguredPlayers().contains(event.getEntity().getUUID())) {
                for (Identifier collectRewardsIdentifier : RewardManager.get().collectRewardsIdentifiers(event.getEntity().getUUID())) {
                    Reward reward = RewardManager.get().getReward(collectRewardsIdentifier);
                    storage.add(event.getEntity().getUUID(), reward.getIdentifier(), reward.getOptions()[0]);
                }
                storage.getConfiguredPlayers().add(event.getEntity().getUUID());
                storage.setDirty();
            }
            CompoundTag nbt = storage.serializeSimple(event.getEntity().level().registryAccess());
            player.level().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> Titanium.NETWORK.sendTo(new RewardSyncMessage(nbt), serverPlayerEntity));
        });
    }

    private void onServerStart(ServerStartingEvent event) {
        RewardCommand.register(event.getServer().getCommands().getDispatcher());
        RewardGrantCommand.register(event.getServer().getCommands().getDispatcher());
    }
}

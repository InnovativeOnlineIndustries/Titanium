/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.command;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.RewardSyncMessage;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RewardCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("titanium-rewards")
                .then(Commands.argument("action", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            return SharedSuggestionProvider.suggest(new String[]{"enable", "disable"}, builder);
                        }).then(Commands.argument("reward", new ResourceLocationArgument())
                                .suggests((context, builder) -> {
                                    return SharedSuggestionProvider.suggest(getAvailableResourceLocations(context).stream().map(ResourceLocation::toString), builder);
                                }).then(Commands.argument("option", StringArgumentType.word()).suggests((context, builder) -> {
                                    return SharedSuggestionProvider.suggest(RewardManager.get().getReward(context.getArgument("reward", ResourceLocation.class)).getOptions(), builder);
                                })
                                        .executes(context -> {
                                            execute(context);
                                            return 1;
                                        })))));

    }

    private static void execute(CommandContext<CommandSourceStack> context) {
        boolean changed = false;
        if (context.getArgument("action", String.class).equalsIgnoreCase("enable")) {
            changed = addReward(context);
        } else {
            changed = removeReward(context);
        }
        if (changed) {
            context.getSource().getServer().execute(() -> {
                CompoundTag nbt = RewardWorldStorage.get(context.getSource().getServer().getLevel(Level.OVERWORLD)).serializeSimple();
                context.getSource().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> Titanium.NETWORK.get().sendTo(new RewardSyncMessage(nbt), serverPlayerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
            });
        }
    }

    private static boolean removeReward(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(source.getLevel());
        try {
            rewardWorldStorage.remove(source.getPlayerOrException().getUUID(), context.getArgument("reward", ResourceLocation.class));
            rewardWorldStorage.setDirty();
            context.getSource().sendSuccess(new TranslatableComponent("titanium.rewards.remove_success"), true);
            return true;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean addReward(CommandContext<CommandSourceStack> context) {
        try {
            CommandSourceStack source = context.getSource();
            ResourceLocation resourceLocation = context.getArgument("reward", ResourceLocation.class);
            if (RewardManager.get().getReward(resourceLocation) == null || !RewardManager.get().getReward(resourceLocation).isPlayerValid(source.getPlayerOrException().getUUID()))
                return false;
            RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(source.getLevel());
            String option = context.getArgument("option", String.class);
            if (!Arrays.asList(RewardManager.get().getReward(resourceLocation).getOptions()).contains(option)) {
                return false;
            }
            rewardWorldStorage.add(source.getPlayerOrException().getUUID(), context.getArgument("reward", ResourceLocation.class), option);
            rewardWorldStorage.setDirty();
            context.getSource().sendSuccess(new TranslatableComponent("titanium.rewards.enabled_success"), true);
            return true;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static List<ResourceLocation> getAvailableResourceLocations(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(source.getLevel());
        List<ResourceLocation> resourceLocations = new ArrayList<>(rewardWorldStorage.getFreeRewards());
        try {
            resourceLocations.addAll(RewardManager.get().collectRewardsResourceLocations(context.getSource().getPlayerOrException().getUUID()));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return resourceLocations;
    }
}

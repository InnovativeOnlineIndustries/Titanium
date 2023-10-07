/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.command;

import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class RewardGrantCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("titanium-rewards-grant")
                .then(Commands.argument("reward", new ResourceLocationArgument()).suggests((context, builder) -> {
                    return ISuggestionProvider.suggest(RewardManager.get().getGiver(context.getSource().asPlayer().getUniqueID(), context.getSource().getName()).getRewards().stream().map(reward -> reward.getResourceLocation().toString()), builder);
                })
                        .executes(context -> {
                            execute(context);
                            return 1;
                        })));
    }

    private static void execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ResourceLocation resourceLocation = context.getArgument("reward", ResourceLocation.class);
        for (Reward reward : RewardManager.get().getGiver(context.getSource().asPlayer().getUniqueID(), context.getSource().getName()).getRewards()) {
            if (reward.getResourceLocation().equals(resourceLocation)) {
                RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(context.getSource().getWorld());
                rewardWorldStorage.addFree(resourceLocation);
                rewardWorldStorage.markDirty();
                context.getSource().sendFeedback(new TranslationTextComponent("titanium.rewards.granted_success"), true);
                return;
            }
        }
    }
}

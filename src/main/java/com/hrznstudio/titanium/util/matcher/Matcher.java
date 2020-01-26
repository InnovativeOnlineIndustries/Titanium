/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock.matcher;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Matcher {

    private final static List<IMatch> MATCH_LIST = new ArrayList<>();

    public static void addMatch(IMatch newMatch) {
        MATCH_LIST.add(newMatch);
    }

    public static ResultHandler matches(BlockState expected, BlockState found, World world, BlockPos pos) {
        return matches(expected, found, world, pos, ImmutableList.of());
    }

    public static ResultHandler matches(BlockState expected, BlockState found, World world, BlockPos pos, List<IMatch> additional) {
        return Stream.concat(MATCH_LIST.stream(), additional.stream())
                .map(pred -> pred.matches(expected, found, world, pos))
                .reduce(ResultHandler.DEFAULT, ResultHandler::combine);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.block_network;

import com.hrznstudio.titanium.block_network.Network;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public abstract class NetworkElement {
    protected final Level level;
    protected final BlockPos pos;

    private final Logger logger = LogManager.getLogger(getClass());
    protected Network network;

    public NetworkElement(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    public void update() {

    }


    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Network getNetwork() {
        return network;
    }

    public void joinNetwork(Network network) {
        this.network = network;

        logger.debug(pos + " joined network " + network.getId());

        sendBlockUpdate();
    }

    public void leaveNetwork() {
        logger.debug(pos + " left network " + network.getId());

        this.network = null;

        sendBlockUpdate();
    }

    public void sendBlockUpdate() {
        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 1 | 2);
    }

    public CompoundTag writeToNbt(CompoundTag tag) {
        tag.putLong("pos", pos.asLong());

        return tag;
    }

    public abstract ResourceLocation getId();

    public abstract ResourceLocation getNetworkType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkElement pipe = (NetworkElement) o;
        return level.equals(pipe.level) &&
            pos.equals(pipe.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, pos);
    }
}

/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block.tile.sideness;

import com.hrznstudio.titanium.client.gui.addon.StateButtonInfo;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.util.HashMap;

public interface IFacingHandler {

    HashMap<EnumFacing, FaceMode> getFacingModes();

    int getColor();

    Rectangle getRectangle();

    String getName();

    enum FaceMode {
        NONE(false, 0), ENABLED(true, 1), PUSH(true, 2), PULL(true, 3);

        private final boolean allowsConnection;
        private final int index;

        FaceMode(boolean allowsConnection, int index) {
            this.allowsConnection = allowsConnection;
            this.index = index;
        }

        public boolean allowsConnection() {
            return allowsConnection;
        }

        public StateButtonInfo getInfo() {
            return new StateButtonInfo(this.getIndex(), null, new String[]{this.name()});//TODO
        }

        public int getIndex() {
            return index;
        }
    }
}
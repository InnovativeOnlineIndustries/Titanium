/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
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

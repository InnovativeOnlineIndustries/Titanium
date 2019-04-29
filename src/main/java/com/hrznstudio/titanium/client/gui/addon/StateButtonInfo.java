/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IAssetType;
import net.minecraft.util.text.TextComponentTranslation;

public class StateButtonInfo {

    private final int state;
    private final IAssetType asset;
    private String[] tooltip;

    public StateButtonInfo(int state, IAssetType asset, String... tooltip) {
        this.state = state;
        this.asset = asset;
        this.tooltip = new String[0];
        if (tooltip != null) {
            this.tooltip = new String[tooltip.length];
            for (int i = 0; i < tooltip.length; i++) {
                this.tooltip[i] = new TextComponentTranslation(tooltip[i]).getFormattedText();
            }
        }
    }

    public int getState() {
        return state;
    }

    public IAssetType getAsset() {
        return asset;
    }

    public String[] getTooltip() {
        return tooltip;
    }

}

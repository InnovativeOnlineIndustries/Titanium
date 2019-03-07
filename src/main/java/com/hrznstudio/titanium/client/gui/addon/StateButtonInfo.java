/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
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

/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.IAssetType;
import net.minecraft.util.text.TranslationTextComponent;

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
                this.tooltip[i] = new TranslationTextComponent(tooltip[i]).getUnformattedComponentText();
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

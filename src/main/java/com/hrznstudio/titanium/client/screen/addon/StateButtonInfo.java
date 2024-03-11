/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.util.LangUtil;
import net.minecraft.util.text.ITextComponent;

public class StateButtonInfo {

    private final int state;
    private final IAssetType asset;
    private ITextComponent[] tooltip;

    public StateButtonInfo(int state, IAssetType asset, String... tooltip) {
        this.state = state;
        this.asset = asset;
        this.tooltip = new ITextComponent[0];
        if (tooltip != null) {
            this.tooltip = new ITextComponent[tooltip.length];
            for (int i = 0; i < tooltip.length; i++) {
                this.tooltip[i] = LangUtil.get(tooltip[i]);
            }
        }
    }

    public int getState() {
        return state;
    }

    public IAssetType getAsset() {
        return asset;
    }

    public ITextComponent[] getTooltip() {
        return tooltip;
    }

}

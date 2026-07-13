/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.tab;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class TitaniumTab {

    private final List<Item> tabList;
    private final Identifier resourceLocation;

    public TitaniumTab(Identifier resourceLocation) {
        this.resourceLocation = resourceLocation;
        this.tabList = new ArrayList<>();
    }

    public List<Item> getTabList() {
        return tabList;
    }

    public Identifier getIdentifier() {
        return resourceLocation;
    }
}

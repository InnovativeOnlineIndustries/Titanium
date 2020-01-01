/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.filter;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;

import java.util.ArrayList;
import java.util.List;

public class MultiFilterComponent implements IGuiAddonProvider {
    public final List<IFilter> filters;

    public MultiFilterComponent() {
        filters = new ArrayList<>();
    }

    public void add(IFilter filter) {
        this.filters.add(filter);
    }

    public List<IFilter> getFilters() {
        return filters;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        filters.forEach(filter -> addons.addAll(filter.getGuiAddons()));
        return addons;
    }
}

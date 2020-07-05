/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.filter;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.component.IComponentHandler;

import java.util.ArrayList;
import java.util.List;

public class MultiFilterComponent implements IScreenAddonProvider, IComponentHandler<IFilter> {
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
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        filters.forEach(filter -> addons.addAll(filter.getScreenAddons()));
        return addons;
    }

    @Override
    public boolean accepts(Object component) {
        return component instanceof IFilter;
    }
}

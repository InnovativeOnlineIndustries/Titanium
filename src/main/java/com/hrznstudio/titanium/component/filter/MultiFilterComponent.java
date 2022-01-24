/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.filter;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.component.IComponentHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiFilterComponent implements IScreenAddonProvider, IComponentHandler {
    public final List<IFilter> filters;

    public MultiFilterComponent() {
        filters = new ArrayList<>();
    }

    public void add(Object... components) {
        Arrays.stream(components).filter(this::accepts).forEach(filter -> this.filters.add((IFilter) filter));
    }

    public List<IFilter> getFilters() {
        return filters;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        filters.forEach(filter -> addons.addAll(filter.getScreenAddons()));
        return addons;
    }

    private boolean accepts(Object component) {
        return component instanceof IFilter;
    }
}

package com.hrznstudio.titanium.block.tile.filter;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;

import java.util.ArrayList;
import java.util.List;

public class MultiFilterHandler implements IGuiAddonProvider {

    public final List<IFilter> filters;

    public MultiFilterHandler() {
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

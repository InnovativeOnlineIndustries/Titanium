package com.hrznstudio.titanium.container.addon;

import com.hrznstudio.titanium.api.IFactory;

import java.util.List;

public interface IContainerAddonProvider {
    List<IFactory<? extends IContainerAddon>> getContainerAddons();
}

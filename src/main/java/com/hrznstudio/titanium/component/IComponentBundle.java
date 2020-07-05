package com.hrznstudio.titanium.component;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;

import javax.annotation.Nullable;

public interface IComponentBundle extends IScreenAddonProvider {

    void accept(@Nullable IComponentHandler<?>... handler);

}

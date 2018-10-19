package com.hrznstudio.titanium.api.component;

public interface IComponentFactory {

    IComponentType getType();

    <T extends IComponent> T createComponent(IComponentTile tile);
}

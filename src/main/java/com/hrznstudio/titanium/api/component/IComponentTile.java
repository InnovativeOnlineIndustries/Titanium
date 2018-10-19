package com.hrznstudio.titanium.api.component;

public interface IComponentTile {

    <T extends IComponent> T getComponent(String componentType);
}

package com.hrznstudio.titanium.components;

import com.hrznstudio.titanium.api.component.IComponent;
import com.hrznstudio.titanium.api.component.IComponentType;

public class GenericComponentType<T extends IComponent> implements IComponentType<T> {
    private String name;
    private Class<T> type;

    public GenericComponentType(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isInstance(IComponent component) {
        return type.isInstance(component);
    }

    @Override
    public T cast(IComponent component) {
        return type.cast(component);
    }

    @Override
    public String getName() {
        return name;
    }
}

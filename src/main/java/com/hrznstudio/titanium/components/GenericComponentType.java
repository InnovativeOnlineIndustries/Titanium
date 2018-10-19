/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
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

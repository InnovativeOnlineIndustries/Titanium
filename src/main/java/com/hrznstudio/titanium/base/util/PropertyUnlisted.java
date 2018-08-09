/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.util;

import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyUnlisted<T> implements IUnlistedProperty<T> {
    protected String name;
    protected Class<T> type;

    protected PropertyUnlisted(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <T> PropertyUnlisted<T> create(String name, Class<T> type) {
        return new PropertyUnlisted<>(name, type);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(T value) {
        return true;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public String valueToString(T value) {
        return value != null ? value.toString() : "null";
    }
}
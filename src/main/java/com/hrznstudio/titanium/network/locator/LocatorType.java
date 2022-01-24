/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator;

import java.util.function.Supplier;

public class LocatorType {
    private final String name;
    private Supplier<LocatorInstance> instanceSupplier;

    public LocatorType(String name, Supplier<LocatorInstance> instanceSupplier) {
        this.name = name;
        this.instanceSupplier = instanceSupplier;
    }

    public LocatorInstance createInstance() {
        return instanceSupplier.get();
    }

    public String getName() {
        return name;
    }
}

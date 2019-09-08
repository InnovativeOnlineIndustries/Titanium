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

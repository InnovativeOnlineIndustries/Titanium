/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.api.client;

import java.util.function.Supplier;

public class GenericAssetType<T extends IAsset> implements IAssetType<T> {
    private Supplier<T> defaultAsset;
    private Class<T> tClass;

    public GenericAssetType(Supplier<T> defaultAsset, Class<T> tClass) {
        this.defaultAsset = defaultAsset;
        this.tClass = tClass;
    }

    @Override
    public T getDefaultAsset() {
        return defaultAsset.get();
    }

    @Override
    public T castOrDefault(IAsset asset) throws ClassCastException {
        if (tClass.isInstance(asset))
            return tClass.cast(asset);
        return defaultAsset.get();
    }
}
/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

public interface IAssetType<T extends IAsset> {

    T getDefaultAsset();

    T castOrDefault(IAsset asset) throws ClassCastException;
}

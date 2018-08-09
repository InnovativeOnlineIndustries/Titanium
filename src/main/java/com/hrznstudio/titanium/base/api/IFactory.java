/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.api;

import javax.annotation.Nonnull;

public interface IFactory<T> {
    @Nonnull
    T create();
}
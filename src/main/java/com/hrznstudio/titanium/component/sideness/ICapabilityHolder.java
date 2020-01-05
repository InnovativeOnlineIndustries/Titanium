/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.sideness;

import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICapabilityHolder<T, R> {
    void add(@Nonnull T capability);

    @Nonnull
    LazyOptional<R> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness);

    boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, IFacingComponent.FaceMode mode);
}
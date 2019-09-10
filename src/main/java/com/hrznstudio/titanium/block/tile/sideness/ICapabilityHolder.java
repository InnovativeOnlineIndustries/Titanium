/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.sideness;

import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public interface ICapabilityHolder<T extends Object, R extends Object> {

    void add(T capability);

    LazyOptional<R> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness);

    boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, IFacingHandler.FaceMode mode);

}

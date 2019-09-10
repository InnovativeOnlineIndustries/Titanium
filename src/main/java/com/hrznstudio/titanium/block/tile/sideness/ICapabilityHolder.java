package com.hrznstudio.titanium.block.tile.sideness;

import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.HashMap;

public interface ICapabilityHolder<T extends Object, R extends Object> {

    void add(T capability);

    HashMap<FacingUtil.Sideness, LazyOptional<R>> getCapabilities();

    LazyOptional<R> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness);

    boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, IFacingHandler.FaceMode mode);

}

package com.hrznstudio.titanium.api.client.assets.types;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.util.EnumFacing;

public interface ITankAsset extends IAsset {

    int getFluidRenderPadding(EnumFacing facing);

}

package com.hrznstudio.titanium.api.redstone;

import net.minecraft.util.IStringSerializable;

public interface IRedstoneAction extends IStringSerializable {

    boolean canRun(IRedstoneState state);

    boolean startsOnChange();

}

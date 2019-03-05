package com.hrznstudio.titanium.client.gui;

import net.minecraft.tileentity.TileEntity;

public interface ITileContainer<T extends TileEntity> {

    public T getTile();

}

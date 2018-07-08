package com.hrznstudio.titanium.api.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IGuiAddonProvider {

    @SideOnly(Side.CLIENT)
    List<? extends IGuiAddon> getGuiAddons();

}

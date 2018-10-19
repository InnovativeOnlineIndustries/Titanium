package com.hrznstudio.titanium.compat.jei;

import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;

public class TitaniumClickArea implements IAdvancedGuiHandler<GuiContainerTile> {
    @Override
    public Class<GuiContainerTile> getGuiContainerClass() {
        return GuiContainerTile.class;
    }

}

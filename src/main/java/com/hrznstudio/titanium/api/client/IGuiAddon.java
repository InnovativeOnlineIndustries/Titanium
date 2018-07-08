package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.gui.GuiContainerTile;

import java.util.List;

public interface IGuiAddon {

    void drawGuiContainerBackgroundLayer(GuiContainerTile container, float partialTicks, int mouseX, int mouseY);

    void drawGuiContainerForegroundLayer(GuiContainerTile container, int mouseX, int mouseY);

    List<String> getTooltipLines();

    boolean isInside(GuiContainerTile container, int mouseX, int mouseY);

}

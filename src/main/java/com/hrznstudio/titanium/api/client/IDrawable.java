package com.hrznstudio.titanium.api.client;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public interface IDrawable {
    void draw(GuiScreen gui, Point position, Point mousePosition);

    static IDrawable of(IAsset asset) {
        return new DefaultDrawable(asset);
    }
}

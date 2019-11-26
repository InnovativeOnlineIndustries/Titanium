package com.hrznstudio.titanium.block.tile.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.ArrowButtonGuiAddon;
import com.hrznstudio.titanium.util.FacingUtil;

import java.util.Collections;
import java.util.List;

public class ArrowButton extends PosButton {

    public final FacingUtil.Sideness direction;

    public ArrowButton(int posX, int posY, int sizeX, int sizeY, FacingUtil.Sideness direction) {
        super(posX, posY, sizeX, sizeY);
        this.direction = direction;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Collections.singletonList(() -> new ArrowButtonGuiAddon(this));
    }

    public FacingUtil.Sideness getDirection() {
        return direction;
    }
}

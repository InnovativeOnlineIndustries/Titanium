/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IEnumValues;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.redstone.IRedstoneAction;
import com.hrznstudio.titanium.block.redstone.RedstoneManager;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.client.screen.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class RedstoneControlButtonComponent<T extends IEnumValues<T> & IRedstoneAction> extends ButtonComponent {

    private final Supplier<RedstoneManager<T>> redstoneManager;
    private final Supplier<BasicTile> componentHarness;
    private boolean isVisible = true;

    public RedstoneControlButtonComponent(int posX, int posY, int sizeX, int sizeY, Supplier<RedstoneManager<T>> redstoneManagerSupplier, Supplier<BasicTile> componentHarness) {
        super(posX, posY, sizeX, sizeY);
        this.redstoneManager = redstoneManagerSupplier;
        this.componentHarness = componentHarness;
        setPredicate((playerEntity, compoundNBT) -> {
            int next = redstoneManager.get().getAction().getValues().indexOf(redstoneManager.get().getAction()) + 1;
            redstoneManager.get().setAction(redstoneManager.get().getAction().getValues().get(next >= redstoneManager.get().getAction().getValues().size() ? 0 : next));
            componentHarness.get().markForUpdate();
        });
    }

    public void show() {
        this.isVisible = true;
    }

    public void hide() {
        this.isVisible = false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        if (isVisible) addons.add(() -> new StateButtonAddon(this) {
            @Override
            public int getState() {
                return redstoneManager.get().getAction().getValues().indexOf(redstoneManager.get().getAction());
            }
        });
        return addons;
    }

}

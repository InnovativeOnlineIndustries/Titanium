/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
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

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class RedstoneControlButtonComponent<T extends IEnumValues<T> & IRedstoneAction> extends ButtonComponent {

    private final Supplier<RedstoneManager<T>> redstoneManager;
    private final Supplier<BasicTile> componentHarness;

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

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new StateButtonAddon(this, new StateButtonInfo(0, AssetTypes.BUTTON_REDSTONE_IGNORED, "tooltip.titanium.redstone.ignored"), new StateButtonInfo(1, AssetTypes.BUTTON_REDSTONE_NO_REDSTONE, "tooltip.titanium.redstone.no_redstone"), new StateButtonInfo(2, AssetTypes.BUTTON_REDSTONE_REDSTONE, "tooltip.titanium.redstone.redstone"), new StateButtonInfo(3, AssetTypes.BUTTON_REDSTONE_ONCE, "tooltip.titanium.redstone.once")) {
            @Override
            public int getState() {
                return redstoneManager.get().getAction().getValues().indexOf(redstoneManager.get().getAction());
            }
        });
    }

}

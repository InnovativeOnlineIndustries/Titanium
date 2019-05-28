/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.proton.Proton;
import com.hrznstudio.titanium.proton.ProtonData;
import com.hrznstudio.titanium.proton.control.ProtonManager;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

@ProtonData(value = "test", description = "Test mod for titanium", defaultActive = false)
public class TestModule extends Proton {

    public TestModule(ProtonManager manager) {
        super(manager);
    }

    @Override
    public void init() {
        EventManager.subscribe(EntityItemPickupEvent.class).filter(entityItemPickupEvent -> entityItemPickupEvent.getItem().getItem().getItem().equals(Items.STICK)).process(entityItemPickupEvent -> entityItemPickupEvent.getItem().lifespan = 0).cancel();
    }

    @Override
    public void addEntries() {
        addEntry(Block.class, BlockTest.TEST = new BlockTest());
        addEntry(Block.class, BlockTwentyFourTest.TEST = new BlockTwentyFourTest());
    }
}

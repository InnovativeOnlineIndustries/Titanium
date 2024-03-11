/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.jei;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class TitaniumJEIPlugin implements IModPlugin {
    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Titanium.MODID, "default");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(BasicContainerScreen.class, new BasicContainerScreenHandler());
    }
}

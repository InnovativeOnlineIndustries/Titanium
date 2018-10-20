package com.hrznstudio.titanium.compat.jei;

import com.hrznstudio.titanium.Titanium;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class TitaniumJEIPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(!shouldRun())
            return;

    }

    @Override
    public void register(IModRegistry registry) {
        if(!shouldRun())
            return;

    }

    public boolean shouldRun() {
        return Titanium.COMPAT_MANAGER.isPulseLoaded("jei");
    }

}

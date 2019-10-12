package com.hrznstudio.titanium.jei;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.gui.container.GuiContainerBase;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class TitaniumJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Titanium.MODID, "default");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(GuiContainerBase.class, new GuiContainerScreenHandler());
    }
}

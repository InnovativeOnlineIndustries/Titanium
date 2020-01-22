package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.multiblock.IMultiblock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

public class TitaniumRegistries {

    public static void init(){
        new RegistryBuilder<IMultiblock>()
                .setName(new ResourceLocation(Titanium.MODID, "multiblock_handlers"))
                .setIDRange(1, Integer.MAX_VALUE - 1)
                .setType(IMultiblock.class)
                .disableSaving()
                .create();
    }

    public static IForgeRegistry<IMultiblock> multiblock_registry = RegistryManager.ACTIVE.getRegistry(IMultiblock.class);
}

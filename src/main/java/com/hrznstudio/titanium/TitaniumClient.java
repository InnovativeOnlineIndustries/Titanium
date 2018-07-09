package com.hrznstudio.titanium;

import com.hrznstudio.titanium.client.TitaniumModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class TitaniumClient {
    public static void registerModelLoader() {
        ModelLoaderRegistry.registerLoader(new TitaniumModelLoader());
    }
}

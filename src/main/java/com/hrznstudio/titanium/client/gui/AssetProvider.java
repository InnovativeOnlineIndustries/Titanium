package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.block.tile.TileBase;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.HashMap;

public class AssetProvider {

    public static HashMap<Class<? extends TileBase>, AssetProvider> ASSETS_PROVIDERS = new HashMap<>();
    private static ResourceLocation ASSETS = new ResourceLocation(Titanium.MODID, "textures/gui/background.png");
    private static AssetProvider DEFAULT_PROVIDER = new AssetProvider();

    public static AssetProvider get(Class<? extends TileBase> clazz) {
        return ASSETS_PROVIDERS.getOrDefault(clazz, DEFAULT_PROVIDER);
    }

    public IAsset getBackground() {
        return new IAsset() {
            @Override
            public ResourceLocation getResourceLocation() {
                return ASSETS;
            }

            @Override
            public Rectangle getArea() {
                return new Rectangle(0, 0, 176, 184);
            }
        };
    }

    public IAsset getSlot() {
        return new IAsset() {
            @Override
            public ResourceLocation getResourceLocation() {
                return ASSETS;
            }

            @Override
            public Rectangle getArea() {
                return new Rectangle(1, 185, 18, 18);
            }
        };
    }

}

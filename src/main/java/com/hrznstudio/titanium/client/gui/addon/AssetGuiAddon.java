package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.gui.screen.Screen;

public class AssetGuiAddon extends BasicGuiAddon {
    private IAsset asset;

    public AssetGuiAddon(IAsset asset, int posX, int posY) {
        super(posX, posY);
        this.asset = asset;
    }

    @Override
    public int getXSize() {
        return asset.getArea().width;
    }

    @Override
    public int getYSize() {
        return asset.getArea().height;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        if (asset != null) {
            AssetUtil.drawAsset(screen, asset, this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {

    }
}

package com.hrznstudio.titanium.cassandra.client.gui;

import com.hrznstudio.titanium.base.api.IFactory;
import com.hrznstudio.titanium.base.api.client.IAsset;
import com.hrznstudio.titanium.base.api.client.IGuiAddon;
import com.hrznstudio.titanium.cassandra.client.gui.addon.ICanMouseDrag;
import com.hrznstudio.titanium.cassandra.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GuiAddonScreen extends GuiScreen {

    private IAssetProvider assetProvider;
    private int x;
    private int y;
    private List<IGuiAddon> addonList;

    private boolean isMouseDragging;
    private int dragX;
    private int dragY;

    public GuiAddonScreen(IAssetProvider assetProvider) {
        this.assetProvider = assetProvider;
    }

    @Override
    public void initGui() {
        super.initGui();
        IAsset background = IAssetProvider.getAsset(assetProvider, IAssetProvider.AssetType.BACKGROUND);
        this.x = this.width / 2 - background.getArea().width / 2;
        this.y = this.height / 2 - background.getArea().height / 2;
        this.addonList = this.guiAddons().stream().map(IFactory::create).collect(Collectors.toList());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        this.checkForMouseDrag(mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, IAssetProvider.AssetType.BACKGROUND).getResourceLocation());
        drawTexturedModalRect(x, y, 0, 0, width, height);
        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerBackgroundLayer(this, assetProvider, x, y, mouseX, mouseY, partialTicks));

        addonList.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerForegroundLayer(this, assetProvider, x, y, mouseX, mouseY));
        for (IGuiAddon iGuiAddon : addonList) {
            if (iGuiAddon.isInside(null, mouseX - x, mouseY - y) && !iGuiAddon.getTooltipLines().isEmpty()) {
                drawHoveringText(iGuiAddon.getTooltipLines(), mouseX - x, mouseY - y);
            }
        }
    }

    public abstract List<IFactory<IGuiAddon>> guiAddons();

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (Mouse.isButtonDown(0)) {
            this.isMouseDragging = true;
            for (IGuiAddon iGuiAddon : this.addonList) {
                if (iGuiAddon instanceof ICanMouseDrag && iGuiAddon.isInside(null, mouseX - x, mouseY - y)) {
                    ((ICanMouseDrag) iGuiAddon).drag(mouseX - dragX, mouseY - dragY);
                }
            }
            this.dragX = mouseX;
            this.dragY = mouseY;
        } else {
            this.isMouseDragging = false;
        }
    }
}
